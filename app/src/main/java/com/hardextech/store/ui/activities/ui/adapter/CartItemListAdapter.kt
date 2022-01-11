package com.hardextech.store.ui.activities.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hardextech.store.R
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.Cart
import com.hardextech.store.ui.activities.ui.activities.CartListActivity
import com.hardextech.store.utils.Constants
import com.hardextech.store.utils.GlideLoader

class CartItemListAdapter(
    private val context: Context,
    private var listOfProductInTheCart: ArrayList<Cart>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // return myViewHolder class in this class
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // create a cart product item list model and get the position for each product
        val cartProductItemListModel = listOfProductInTheCart[position]

        if (holder is MyViewHolder){
            // assign the image to the cart image view in the allocated resource layout
            GlideLoader(context).loadProductImage(cartProductItemListModel.product_image, holder.itemView.findViewById(R.id.iv_cart_item_image))
            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_title).text = cartProductItemListModel.product_title
            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_price).text = "NGN ${cartProductItemListModel.product_price}"
            holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text = cartProductItemListModel.cart_quantity

            // letting the users or buyers knows when a product is out of stock

            if (cartProductItemListModel.cart_quantity == "0"){
                // disabling the user to increase or decrease the number of product order
                holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility = View.GONE
                holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.GONE
                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text = context.resources.getString(R.string.out_of_stock)
                // change the text size for the OUT OF STOCK from 16sp to 12sp
                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
                // changing the textColor
                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).setTextColor(ContextCompat.getColor(context, R.color.out_of_stock_text_color))

            } else{
                // in cases where product is not out of stock
                holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility = View.VISIBLE
                holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.VISIBLE
                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText))
            }

            // enabling the delete button in the cart activity
            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).setOnClickListener {
                when(context){
                    is CartListActivity->{
                        val alertDialog = AlertDialog.Builder(context).setTitle("DELETE PRODUCT")
                            .setMessage("Are you sure you want to delete this product, (${cartProductItemListModel.product_title}) from your cart")
                            .setIcon(android.R.drawable.ic_dialog_alert)

                            .setPositiveButton("Yes") {dialogueInterface, _ ->
                                context.showProgressDialogue("Deleting ${cartProductItemListModel.product_title}")
                                FirestoreClass().deleteItemFromCart(context, cartProductItemListModel.id)
                                dialogueInterface.dismiss()
                            }

                            .setNegativeButton("No") { dialogueInterface, _ ->
                                dialogueInterface.dismiss()
                            }

                        val getAlertDialogue: AlertDialog = alertDialog.create()
                        getAlertDialogue.setCancelable(false)
                        getAlertDialogue.show()

                        getAlertDialogue.findViewById<ImageView>(android.R.id.icon)
                            ?.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)


                      //  context.showProgressDialogue("Deleting ${cartProductItemListModel.product_title}")
                    }
                }
               // FirestoreClass().deleteItemFromCart(context, cartProductItemListModel.id)
            }

            // enabling the ib_remove_cart_item and ib_add_cart_item
            holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).setOnClickListener {
                // when the user clicks on the remove imageButton
                if (cartProductItemListModel.cart_quantity == "1"){
                    // perform the same end function as when the user clicks on the delete button
                    FirestoreClass().deleteItemFromCart(context, cartProductItemListModel.id)
                   } else{
                       // converting the cart_quantity from string to Int
                       val cartQuantity: Int = cartProductItemListModel.cart_quantity.toInt()
                    // creating an HashMap for updating the changes
                       val itemHashMap = HashMap<String, Any>()
                       itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                       //show the progress Dialogue
                       if (context is CartListActivity){
                           context.showProgressDialogue("Updating Cart")
                       }
                       FirestoreClass().updateMyCartItem(context, cartProductItemListModel.id, itemHashMap)
                   }


            }
          holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).setOnClickListener {
              val cartQuantity: Int = cartProductItemListModel.cart_quantity.toInt()
              if (cartQuantity < cartProductItemListModel.product_quantity.toInt()){
                  val itemHashmap = HashMap<String, Any>()
                  itemHashmap[Constants.CART_QUANTITY] = (cartQuantity+1).toString()
                  if (context is CartListActivity){
                      context.showProgressDialogue("Updating")
                  }
                  FirestoreClass().updateMyCartItem(context, cartProductItemListModel.id, itemHashmap)
              }else{
                  if (context is CartListActivity){
                      Toast.makeText(context, "The available Product Quantity is ${cartProductItemListModel.product_quantity}, you can not order more than the available stock", Toast.LENGTH_LONG).show()
                  }
              }
          }
        }
    }

    override fun getItemCount(): Int {
       // return the numbers of items in the cart
       return listOfProductInTheCart.size

    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}