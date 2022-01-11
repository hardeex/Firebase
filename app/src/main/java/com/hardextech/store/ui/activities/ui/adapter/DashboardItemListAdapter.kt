package com.hardextech.store.ui.activities.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hardextech.store.R
import com.hardextech.store.model.Product
import com.hardextech.store.utils.GlideLoader

class DashboardItemListAdapter(
    private val context: Context,
    private var productList: ArrayList<Product>
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener:MyInterfaceOnclickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_dashboard_layout, parent, false)
        )
    }

    fun setOnclickListener(onCLickListerMyCustomClick: MyInterfaceOnclickListener){
        // making the onClickListener my custom onCLickListener
        this.onClickListener = onCLickListerMyCustomClick
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        // displaying the individual list entries

        val model = productList[position]
        if (viewHolder is MyViewHolder) {
            GlideLoader(context).loadProductImage(
                model.product_image, viewHolder.itemView.findViewById(
                    R.id.iv_dashboard_item_image
                )
            )
            viewHolder.itemView.findViewById<TextView>(R.id.tv_dashboard_item_title).text = model.product_title
            viewHolder.itemView.findViewById<TextView>(R.id.tv_dashboard_item_price).text = "NGN ${model.product_price}"

            // assigning my custom onClickListener to the recycler viewHolder items--- to my items in the dashboard clickable
            viewHolder.itemView.setOnClickListener {
                if (onClickListener!=null){
                    onClickListener!!.onClick(position, model)
                }
            } // end of the custom viewHolder for the dashboard item
        }
    } // end of the onBindViewHolder()

    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface MyInterfaceOnclickListener{
        // to integrate an onclickListener on the products on the Dashboard
        fun onClick(position: Int, product: Product)
    }
}