package com.hardextech.store.ui.activities.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardextech.store.R
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.Cart
import com.hardextech.store.model.Product
import com.hardextech.store.ui.activities.ui.adapter.CartItemListAdapter

class CartListActivity : BaseActivity() {

    private lateinit var rv_cart_item_list: RecyclerView
    private lateinit var tv_no_cart_item_found: TextView
    private lateinit var tv_product_subTotal: TextView
    private lateinit var tv_delivery_cost: TextView
    private lateinit var tv_total_product_cost: TextView
    private lateinit var btn_checkout_cart_item: Button
    private lateinit var ll_check_out: LinearLayout

    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartDataClassDetails:  ArrayList<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        // setting the action bar title
        supportActionBar?.title = "MY CART"
        // enabling the back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // setting the back arrow symbol
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        // changing the action color to the exact gradient at the bottomNav
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this, R. drawable.fragment_bottom_color))

        initializeVariables()
    } // end of the onCreate method

    private fun initializeVariables() {
        rv_cart_item_list = findViewById(R.id.rv_cart_items_list)
        tv_no_cart_item_found = findViewById(R.id.tv_no_cart_item_found)
        tv_product_subTotal=findViewById(R.id.tv_sub_total_value)
        tv_delivery_cost= findViewById(R.id.tv_delivery_cost_value)
        tv_total_product_cost = findViewById(R.id.tv_total_product_cost)
        btn_checkout_cart_item = findViewById(R.id.btn_checkout_cart_item_list)
        ll_check_out = findViewById(R.id.ll_checkout)
    }

    fun successfullyGetTheCartItemList(listOfItemInTheCart: ArrayList<Cart>){
        dismissProgressDialogue()
        // checking for the product list in the stock-- determining if the product is available in the stock
        for (everyProductInTheProductList in mProductList){
            for (everyProductInTheCart in listOfItemInTheCart){
                if (everyProductInTheProductList.product_id == everyProductInTheCart.product_id){
                    everyProductInTheCart.product_quantity = everyProductInTheProductList.product_quantity

                    // if there are no products in the stock
                    if (everyProductInTheProductList.product_quantity.toInt() == 0){
                        everyProductInTheCart.cart_quantity = everyProductInTheProductList.product_quantity
                    }
                }
            }
        }
        // initializing the mCartDataClassDetails
        mCartDataClassDetails = listOfItemInTheCart

        // checking for the product list in the cart
        if (mCartDataClassDetails.size >0){
            rv_cart_item_list.visibility = View.VISIBLE
            ll_check_out.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_item_list.layoutManager = LinearLayoutManager(this)
            rv_cart_item_list.setHasFixedSize(true)
            // create an adapter variable for the recycler view
            val cartListAdapter = CartItemListAdapter(this, listOfItemInTheCart)
            // pass the adapter to the recyclerview
            rv_cart_item_list.adapter = cartListAdapter
            // assign double to the sub-total in the itemListCart
            var subTotal: Double = 0.0
            // run through all product in the list
            for (everyItemInTheCart in mCartDataClassDetails){
                // checking for the available quantity of product
                   val availableProductQuantity = everyItemInTheCart.product_quantity.toInt()
                if (availableProductQuantity > 0){
                        Log.i("Cart Title", everyItemInTheCart.product_title)
                        // convert the price to Double
                    //    val price = everyItemInTheCart.product_price.toDouble()
                        val price = everyItemInTheCart.product_price.toDouble()
                        // convert the quantity to Int
                        val quantity = everyItemInTheCart.cart_quantity.toInt()
                        // calculate the sub-total cost
                        subTotal+=(price*quantity)
                    }

            }
            // assign the value of the sub total for each product sales
            tv_product_subTotal.text = "NGN $subTotal"
            // assigning the delivery cost for each product sales
            // TODO: Seek for API to calculate the delivery cost for each product and/or write the code criteria for calculating it
            tv_delivery_cost.text = (subTotal*0.05).toString()

            if (subTotal > 0){
                ll_check_out.visibility = View.VISIBLE
                // TODO: Change the logic for the delivery cost
                val totalProductCost = subTotal + (subTotal*0.05)
                tv_total_product_cost.text = "NGN $totalProductCost"
            } else{
                ll_check_out.visibility = View.GONE
            }
        } else{
            rv_cart_item_list.visibility = View.GONE
            ll_check_out.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    fun successfullyGetProductFromFirestore(productList: ArrayList<Product>){
        dismissProgressDialogue()
        mProductList= productList
        getCartItemListFromFirestore()
    }

    fun deleteCartItemSuccessfully(){
        dismissProgressDialogue()
        Toast.makeText(this, "Product Successfully deleted from the cart", Toast.LENGTH_SHORT).show()
        getCartItemListFromFirestore()
    }

    fun updateCartItemSuccessfullyFromFirestore(){
        dismissProgressDialogue()
        getCartItemListFromFirestore()
    }


    private fun getProductListFromFirestore(){
        showProgressDialogue("Getting Product Lists")
        FirestoreClass().getAllProductList(this)
    }

    private fun getCartItemListFromFirestore(){
       // showProgressDialogue("Please Wait")
        FirestoreClass().getCartList(this)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
       // getCartItemListFromFirestore()
        getProductListFromFirestore()
    }
} // end of the class