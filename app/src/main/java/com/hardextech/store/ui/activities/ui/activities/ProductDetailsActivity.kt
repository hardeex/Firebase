package com.hardextech.store.ui.activities.ui.activities


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.hardextech.store.R
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.Product
import com.hardextech.store.utils.Constants
import com.hardextech.store.utils.GlideLoader

class ProductDetailsActivity : BaseActivity() {

    private lateinit var iv_product_details_activity: ImageView
    private lateinit var tv_title_productDetailsActivity: TextView
    private lateinit var tv_price_productDetailsActivity: TextView
    private lateinit var tv_description_productDetailsActivity: TextView
    private lateinit var tv_descriptionCONTENT_productDetailsActivity: TextView
    private lateinit var tv_quantity_productDetailsActivity: TextView
    private lateinit var tv_quantityVALUE_productDetailsActivity: TextView

    private var mProductID: String = ""
    private var product_id: String = ""
    //  private  var mProductDetails:Product = Product()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        // setting the action bar title
        supportActionBar?.title = "PRODUCT DETAILS"
        // enabling the back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // setting the back arrow symbol
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        // changing the action color to the exact gradient at the bottomNav
        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.fragment_bottom_color
            )
        )



        initializeVariable()


        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductID = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product Id", mProductID)
        }


        getProductDetails()


    } //  end of the onCreate

    private fun initializeVariable() {
        iv_product_details_activity = findViewById(R.id.iv_product_detail_image)
        tv_title_productDetailsActivity = findViewById(R.id.tv_product_details_title)
        tv_price_productDetailsActivity = findViewById(R.id.tv_product_details_price)
        tv_description_productDetailsActivity =
            findViewById(R.id.tv_description_header_product_details_label)
        tv_descriptionCONTENT_productDetailsActivity =
            findViewById(R.id.tv_product_details_description)
        tv_quantity_productDetailsActivity = findViewById(R.id.tv_product_details_quantity)
        tv_quantityVALUE_productDetailsActivity =
            findViewById(R.id.tv_product_details_stock_quantity)
    }


    fun productDetailsRetrievedSuccessfully(productDetails: Product) {
        dismissProgressDialogue()
        GlideLoader(this).loadProductImage(
            productDetails.product_image,
            iv_product_details_activity
        )
        tv_title_productDetailsActivity.text = productDetails.product_title
        tv_price_productDetailsActivity.text = "NGN ${productDetails.product_price}"
        tv_descriptionCONTENT_productDetailsActivity.text = productDetails.product_description
        tv_quantityVALUE_productDetailsActivity.text = productDetails.product_quantity.toString()

        // validating that the product is not out of stock
        if (productDetails.product_quantity.toInt() == 0){
            dismissProgressDialogue()
            tv_quantityVALUE_productDetailsActivity.text = resources.getString(R.string.out_of_stock)
            tv_quantityVALUE_productDetailsActivity.setTextColor(ContextCompat.getColor(this, R.color.out_of_stock_text_color))

        }
    }

    private fun getProductDetails() {
        showProgressDialogue("Please wait")
        FirestoreClass().getProductDetail(this, mProductID)
    }

    override fun onSupportNavigateUp(): Boolean {
        // navigating the back arrow at the action bar to the previous activity
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // creating an edit icon on the ProductDetailsActivity
        menuInflater.inflate(R.menu.edit_product_details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Performing actions when the menu items in the ProductDetailsActivity is/are selected
        when (item.itemId) {
            R.id.edit_product_details_menu -> {
             //  val products = Product()
               // productDetailsInfoPassedSuccessfully(products)
              //  FirestoreClass().getProductInfo(this)
            }


        } // end of the when statement
        return super.onOptionsItemSelected(item)
    }

    fun productDetailsInfoPassedSuccessfully(product: Product?) {
        val intent = Intent(this@ProductDetailsActivity, AddProductActivity::class.java)
        intent.putExtra(Constants.EDIT_EXTRA_PRODUCT_ID, product)
        startActivity(intent)

       // FirestoreClass().getProductDetail(this, mProductID)


    }
}


