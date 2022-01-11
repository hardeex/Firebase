package com.hardextech.store.ui.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.hardextech.store.R
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.Cart
import com.hardextech.store.model.Product
import com.hardextech.store.utils.Constants
import com.hardextech.store.utils.GlideLoader

class DashboardProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var iv_dashboard_product_details_activity:ImageView
    private lateinit var tv_dashboard_title_productDetailsActivity:TextView
    private lateinit var tv_dashboard_price_productDetailsActivity:TextView
    private lateinit var tv_dashboard_description_productDetailsActivity:TextView
    private lateinit var tv_dashboard_descriptionCONTENT_productDetailsActivity:TextView
    private lateinit var tv_dashboard_quantity_productDetailsActivity:TextView
    private lateinit var tv_dashboard_quantityVALUE_productDetailsActivity:TextView
    private lateinit var btnAddToCart: Button
    private lateinit var btnGoToCart: Button

    private var mDashboardProductID: String = ""
    private lateinit var mProductDetails: Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_product_details)

        initializeVariable()

        // Enabling the onClickListener for the implemented views
        btnAddToCart.setOnClickListener(this)
        btnGoToCart.setOnClickListener(this)

        if (intent.hasExtra(Constants.DASHBOARD_EXTRA_PRODUCT_ID)){
           mDashboardProductID = intent.getStringExtra(Constants.DASHBOARD_EXTRA_PRODUCT_ID)!!
            Log.i("Product Id", mDashboardProductID)
        }
        var productOwnerID: String=""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            productOwnerID = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
            Log.i("Product Id", productOwnerID)
        }

        if (FirestoreClass().getCurrentUserID() == productOwnerID){
            btnAddToCart.visibility = View.GONE
            btnGoToCart.visibility = View.GONE
        }else{
            btnAddToCart.visibility = View.VISIBLE
            btnGoToCart.visibility = View.VISIBLE
        }

        // setting the action bar title
        supportActionBar?.title = "PRODUCT DETAILS"
        // enabling the back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // setting the back arrow symbol
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        // changing the action color to the exact gradient at the bottomNav
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this, R. drawable.fragment_bottom_color))


        getProductDetails()

    } // enf of the onCreate method

    private fun initializeVariable() {
        iv_dashboard_product_details_activity = findViewById(R.id.iv_dashboard_product_detail_image)
        tv_dashboard_title_productDetailsActivity=findViewById(R.id.tv_dashboard_product_details_title)
        tv_dashboard_price_productDetailsActivity = findViewById(R.id.tv_dashboard_product_details_price)
        tv_dashboard_description_productDetailsActivity= findViewById(R.id.tv_dashboard_description_header_product_details_label)
        tv_dashboard_descriptionCONTENT_productDetailsActivity = findViewById(R.id.tv_dashboard_product_details_description)
        tv_dashboard_quantity_productDetailsActivity=findViewById(R.id.tv_dashboard_product_details_quantity)
        tv_dashboard_quantityVALUE_productDetailsActivity=findViewById(R.id.tv_dashboard_product_details_stock_quantity)
        btnAddToCart = findViewById(R.id.btn_addToCart)
        btnGoToCart = findViewById(R.id.btn_GoToCart)
    }

    fun productDetailsRetrievedSuccessfully(productDetails:Product){
        // initializing the mProductDetails and storing the product object in it
        mProductDetails = productDetails
        GlideLoader(this).loadProductImage(productDetails.product_image, iv_dashboard_product_details_activity)
        tv_dashboard_title_productDetailsActivity.text = productDetails.product_title
        tv_dashboard_price_productDetailsActivity.text = "NGN ${productDetails.product_price}"
        tv_dashboard_descriptionCONTENT_productDetailsActivity.text = productDetails.product_description
        tv_dashboard_quantityVALUE_productDetailsActivity.text = productDetails.product_quantity.toString()

        // validating that the product is not out of stock
        if (productDetails.product_quantity.toInt() == 0){
            dismissProgressDialogue()
            btnAddToCart.visibility = View.GONE
            tv_dashboard_quantityVALUE_productDetailsActivity.text = resources.getString(R.string.out_of_stock)
           tv_dashboard_quantityVALUE_productDetailsActivity.setTextColor(ContextCompat.getColor(this, R.color.out_of_stock_text_color))
        } else{
            if (FirestoreClass().getCurrentUserID() == productDetails.user_id){
                dismissProgressDialogue()
            }else{
                FirestoreClass().checkIfProductExistInCart(this, mDashboardProductID)
            }
        }


    }

    private fun getProductDetails(){
        showProgressDialogue("Please wait")
        FirestoreClass().getProductDetail(this, mDashboardProductID)


    }

    private fun addProductTOCart(){
        val cartItem = Cart(
            FirestoreClass().getCurrentUserID(),
            mDashboardProductID,
            mProductDetails.product_title,
            mProductDetails.product_price,
            mProductDetails.product_image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialogue("Adding Product To Cart")
        FirestoreClass().addCartItemsToFirestore(this, cartItem)
    }

    fun productAddedToCartSuccessfully(){
        dismissProgressDialogue()
        Toast.makeText(this, "You have added this product to your cart", Toast.LENGTH_SHORT).show()

        btnAddToCart.visibility = View.GONE
        btnGoToCart.visibility = View.VISIBLE
    }

    fun checkIfProductExistInCart(){
        // if the item is in the cart-- You can't add anymore
        dismissProgressDialogue()
        btnAddToCart.visibility = View.GONE
        btnGoToCart.visibility = View.VISIBLE
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.btn_addToCart->{
                    addProductTOCart()
                }

                R.id.btn_GoToCart->{
                    startActivity(Intent(this, CartListActivity::class.java))
                }
            }
        }
    }

} // end of the class