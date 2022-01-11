package com.hardextech.store.ui.activities.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hardextech.store.R
import com.hardextech.store.model.Product
import com.hardextech.store.ui.activities.ui.activities.ProductDetailsActivity
import com.hardextech.store.ui.activities.ui.fragments.ProductsFragment
import com.hardextech.store.utils.Constants
import com.hardextech.store.utils.GlideLoader

open class ProductListAdapter(
    private val context:Context,
    private var productList: ArrayList<Product>,
    private val productsFragment: ProductsFragment
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        // displaying the individual list entries

        val model = productList[position]
        if (viewHolder is MyViewHolder){
            GlideLoader(context).loadProductImage(model.product_image, viewHolder.itemView.findViewById(R.id.iv_product_image_itemListLayout))
            viewHolder.itemView.findViewById<TextView>(R.id.tv_product_name_itemListLayout).text = model.product_title
            viewHolder.itemView.findViewById<TextView>(R.id.tv_product_price_itemListLayout).text = "NGN ${model.product_price}"

            val ib_vector_delete_product = viewHolder.itemView.findViewById<ImageButton>(R.id.ib_delete_product)
            ib_vector_delete_product.setOnClickListener {
                productsFragment.deleteProductFromFirestore(model.product_id)
            }

            viewHolder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                // passing the UI to be displayed to each individual user
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}