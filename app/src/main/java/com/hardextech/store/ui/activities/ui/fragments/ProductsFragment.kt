package com.hardextech.store.ui.activities.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardextech.store.R
//import com.hardextech.store.databinding.FragmentHomeBinding
import com.hardextech.store.databinding.FragmentProductsBinding
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.Product
import com.hardextech.store.ui.activities.ui.activities.AddProductActivity
import com.hardextech.store.ui.activities.ui.adapter.ProductListAdapter

class ProductsFragment : BaseFragment() {

     lateinit var rv_my_product_item: RecyclerView
     lateinit var tv_no_product_found: TextView



   // private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // call when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // to enable menu option in a fragment
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // for accessing the views in the fragment
        rv_my_product_item = view.findViewById(R.id.rv_my_product_items)
        tv_no_product_found = view.findViewById(R.id.tv_no_products_found)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)


        //   val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        // textView.text = "Home"
        return binding.root


    }

    fun deleteProductFromFirestore(product_id: String){
       // Toast.makeText(requireActivity(), "$product_id Deleted Successfully", Toast.LENGTH_SHORT).show()
        showAlertDialog(product_id)
    }

    fun successfullyDeletedProductFromFirestore(){
        dismissProgressDialogue()
        Toast.makeText(requireActivity(), "Product Deleted Successfully", Toast.LENGTH_SHORT).show()
        getProductListFromFirestore()
    }

    private fun showAlertDialog(product_id: String) {
        val alertDialog = AlertDialog.Builder(requireActivity()).setTitle("DELETE PRODUCT")
            .setMessage("Are you sure you want to delete this product? ($product_id)")
            .setIcon(android.R.drawable.ic_dialog_alert)

            .setPositiveButton("Yes") {dialogueInterface, _ ->
                showProgressDialogue("Deleting $product_id...")
                FirestoreClass().deleteProductFromCloudFirestore(this, product_id)
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
    }


    fun successfullyUploadedProductListFromFirestore(productList:ArrayList<Product>){
        dismissProgressDialogue()

//        for (i in productList){
//            Log.i("Product Name", i.product_title)
//        }

//        rv_my_product_item = RecyclerView(requireActivity())
//        tv_no_product_found = TextView(requireActivity())
        if (productList.size > 0){
            rv_my_product_item.visibility = View.VISIBLE
            tv_no_product_found.visibility = View.GONE
            rv_my_product_item.layoutManager = LinearLayoutManager(activity)
            rv_my_product_item.setHasFixedSize(true)

            val productAdapter = ProductListAdapter(requireActivity(), productList, this)
            rv_my_product_item.adapter = productAdapter
        } else{
            rv_my_product_item.visibility = View.GONE
            tv_no_product_found.visibility = View.VISIBLE
        }
    }



    private fun getProductListFromFirestore(){
        showProgressDialogue("Retrieving Product Lists")
        FirestoreClass().getProductList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFirestore()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // in order to create the sub menu or menu in the fragment
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_products_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_product_menu_item->{
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
