package com.hardextech.store.ui.activities.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardextech.store.R
import com.hardextech.store.databinding.FragmentDashboardBinding
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.Product
import com.hardextech.store.ui.activities.ui.activities.CartListActivity
import com.hardextech.store.ui.activities.ui.activities.DashboardProductDetailsActivity
import com.hardextech.store.ui.activities.ui.activities.ProductDetailsActivity
import com.hardextech.store.ui.activities.ui.activities.SettingActivity
import com.hardextech.store.ui.activities.ui.adapter.DashboardItemListAdapter
import com.hardextech.store.utils.Constants

 class DashboardFragment : BaseFragment() {

    lateinit var rv_my_dashboard_item: RecyclerView
    lateinit var tv_dashboard_no_found: TextView

  //  private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

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
        rv_my_dashboard_item = view.findViewById(R.id.rv_dashboard_items)
        tv_dashboard_no_found= view.findViewById(R.id.tv_no_dashboard_items_found)
    }

    // call whenever the view is created
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemListFromFirestore()
    }

    // in order to create the sub menu or menu in the fragment
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_setting->{
                startActivity(Intent(activity, SettingActivity::class.java))
                return true
            }

            R.id.action_cart->{
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successfullyUploadedDashboardItemListFromFirestore(dashBoardItemList: ArrayList<Product>) {
        dismissProgressDialogue()
//        for (i in dashBoardItemList){
//            Log.i("Item List Title", i.product_title)
//        }

        if (dashBoardItemList.size > 0) {
            rv_my_dashboard_item.visibility = View.VISIBLE
            tv_dashboard_no_found.visibility = View.GONE
            rv_my_dashboard_item.layoutManager = GridLayoutManager(activity, 2)
            rv_my_dashboard_item.setHasFixedSize(true)
            val dashboardItemListAdapter =
                DashboardItemListAdapter(requireActivity(), dashBoardItemList)
            rv_my_dashboard_item.adapter = dashboardItemListAdapter

            dashboardItemListAdapter.setOnclickListener(object :
                DashboardItemListAdapter.MyInterfaceOnclickListener {
                override fun onClick(position: Int, product: Product) {
                   // val intent = Intent(context, ProductDetailsActivity::class.java)
                    val intent = Intent(context, DashboardProductDetailsActivity::class.java)
                    // passing the product details data
                    intent.putExtra(Constants.DASHBOARD_EXTRA_PRODUCT_ID, product.product_id)
                    // passing the UI to be displayed to each individual user
                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, product.user_id)
                    startActivity(intent)
                }

            })
        } else {
            rv_my_dashboard_item.visibility = View.GONE
            tv_dashboard_no_found.visibility = View.VISIBLE
        }
    }

    private fun getDashboardItemListFromFirestore() {
        showProgressDialogue("Please Wait")
        FirestoreClass().getDashboardItemList(this)
    }
} // end of the fragment class