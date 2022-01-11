package com.hardextech.store.ui.activities.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.hardextech.store.R
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.Product
import com.hardextech.store.utils.Constants
import com.hardextech.store.utils.GlideLoader
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private lateinit var iv_product_image_layout: ImageView
    private lateinit var iv_add_product_image: ImageView
    private lateinit var tv_product_title: TextInputLayout
    private lateinit var tv_product_price: TextInputLayout
    private lateinit var tv_product_description: TextInputLayout
    private lateinit var tv_product_quantity: TextInputLayout
    private lateinit var btnSubmit_addProduct: Button


    private var mSelectedImageFIleURI: Uri? = null
    private var mProductImageURI: String = ""

  //  private  var mProductDetails:Product? = null

    private var mProductDetails_HashMap : String = ""

    private var mProductIDs: String? = ""
    private lateinit var  mProductDetails: Product



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // setting the action bar text --- the activity title
       supportActionBar?.title = "ADD PRODUCT"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // setting the back arrow symbol
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        // changing the action color to the exact gradient at the bottomNav
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.fragment_bottom_color))

        initiateVariables()

        iv_add_product_image.setOnClickListener(this)
        btnSubmit_addProduct.setOnClickListener(this)

//        val sharedPreference = getSharedPreferences(Constants.PRODUCT_SHARED_PREFERENCE, Context.MODE_PRIVATE)
//        val productLoggedInUser = sharedPreference.getString(Constants.PRODUCT_LOGGED_IN, null)
//        Toast.makeText(this, productLoggedInUser, Toast.LENGTH_LONG).show()

       // getProductDetailsForEdit()
        var productDetails: Product = Product()
        if (intent.hasExtra(Constants.EDIT_EXTRA_PRODUCT_ID)){
            productDetails= intent.getParcelableExtra(Constants.EDIT_EXTRA_PRODUCT_ID)!!

        }

        tv_product_title.editText?.setText(productDetails.product_title)
        tv_product_price.editText?.setText(productDetails.product_price)
        tv_product_description.editText?.setText(productDetails.product_description)
        tv_product_quantity.editText?.setText(productDetails.product_quantity)

        //getProductDetailsToBeEdited()

    } //end of the onCreate method

//    private fun getProductDetailsToBeEdited() {
//        showProgressDialogue("Please wait")
//        FirestoreClass().getProductDetail(this, mProductIDs)
//    }

    fun productDetailsRetrievedSuccessfully(productDetails:Product){
        dismissProgressDialogue()
        GlideLoader(this).loadProductImage(productDetails.product_image, iv_product_image_layout)
        tv_product_title.editText?.setText(productDetails.product_title)
        tv_product_price.editText?.setText(productDetails.product_price)
        tv_product_description.editText?.setText(productDetails.product_description)
        tv_product_quantity.editText?.setText(productDetails.product_quantity)
    }

//    private fun getProductDetailsForEdit(){
//
//     //   var productDetails: Product = Product()
//            if (intent.hasExtra(Constants.EDIT_PRODUCT_ID)){
//                // Get the product details from the intent as a ParcelableExtra
//                mProductDetails = intent.getParcelableExtra(Constants.EDIT_PRODUCT_ID)!!
//            }
//
//            tv_product_title.editText?.setText(mProductDetails.product_title)
//            tv_product_price.editText?.setText(mProductDetails.product_price)
//            tv_product_description.editText?.setText(mProductDetails.product_description)
//            tv_product_quantity.editText?.setText(mProductDetails.product_quantity)
//
//
//    }

    private fun initiateVariables() {
        iv_product_image_layout=findViewById(R.id.iv_product_image_layout)
        iv_add_product_image = findViewById(R.id.iv_add_product_image)
        tv_product_description = findViewById(R.id.product_description)
        tv_product_quantity = findViewById(R.id.product_quantity)
        tv_product_title = findViewById(R.id.product_title)
        tv_product_price = findViewById(R.id.product_price)
        btnSubmit_addProduct = findViewById(R.id.btnSubmit_addProduct)
    }

    private fun validateProductDetails(): Boolean{
        when{
            mSelectedImageFIleURI == null ->{
                Toast.makeText(this, "Please, add the product image", Toast.LENGTH_SHORT).show()
                return false
            }

            TextUtils.isEmpty(tv_product_title.editText?.text.toString().trim{it<=' '})->{
                tv_product_title.error = " Please, enter the product title"
                return false
            }

            TextUtils.isEmpty(tv_product_price.editText?.text.toString().trim{it<=' '})->{
                tv_product_price.error = "Please, enter the product price"
                return false
            }

            TextUtils.isEmpty(tv_product_description.editText?.text.toString().trim{it<=' '})->{
                tv_product_description.error = "Please, enter information about the product you are selling"
                return false
            }

            TextUtils.isEmpty(tv_product_quantity.editText?.text.toString().trim{it<=' '})->{
                tv_product_quantity.error = "Please, enter the number of product available for sales"
                return false
            }

            else->{
                return true
            }
        }
    }

    private fun uploadProductImage(){
        showProgressDialogue("Uploading Product")
        FirestoreClass().uploadImagesToCloudStorage(this, mSelectedImageFIleURI, Constants.PRODUCT_IMAGE)
    }
    fun uploadProductImageSuccessfully(imageUri: String){
//         dismissProgressDialogue()
//         Toast.makeText(this, "Image uploaded successfully \n Path: $imageUri", Toast.LENGTH_LONG).show()

        mProductImageURI = imageUri
        uploadProductDetails()

    }

    private fun uploadProductDetails(){
        val user_name = this.getSharedPreferences(Constants.JDD_STORE_PREFERENCE, Context.MODE_PRIVATE).getString(Constants.LOGGED_IN_USER, "")!!

        // the product information to be uploaded to the database
        val product = Product(
                FirestoreClass().getCurrentUserID(),
                user_name,
                mProductImageURI,
                tv_product_title.editText?.text.toString().trim { it<=' ' },
                tv_product_price.editText?.text.toString().trim { it<=' ' },
                tv_product_description.editText?.text.toString().trim { it<=' ' },
                tv_product_quantity.editText?.text.toString().trim { it<=' ' }

            )


            FirestoreClass().uploadProductDetails(this, product)

    }


     fun productUploadSuccessfully(){
        dismissProgressDialogue()
        Toast.makeText(this, "Products upload is successful", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){

                R.id.iv_add_product_image->{
                    // check and request for permission when the user try to upload product the first time
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
                        Constants.showImageChooser(this)
                    } else{
                        // if permission was not granted --- Request Permission
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_EXTERNAL_STORAGE_CODE)
                    }
                } // end of the R.id.iv_add_product_image

                R.id.btnSubmit_addProduct->{
                    if (validateProductDetails()){
                       uploadProductImage()
                    }
                } // end of the R.id.btnSubmit_addProduct


            } // end of the when statement
        } // end of the onCLick IF STATEMENT
    } // end of the onClick ViewListener

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==Constants.READ_EXTERNAL_STORAGE_CODE){
            // if permission is granted
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            } else{
                // if permission was declined
                Toast.makeText(this,
                    "Storage Permission required for uploading your photo/image. You can also allow permission manually from your device settings",
                    Toast.LENGTH_LONG).show()
            }
        }
    } // end of endRequestPermissionResult method

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode==Constants.PICK_IMAGE_REQUEST_CODE){
                if (data!=null){
                    // change the icon to edit mode once product is uploaded
                  iv_add_product_image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))

                    mSelectedImageFIleURI= data.data!!
                    try {
                        GlideLoader(this).loadUserImage(mSelectedImageFIleURI!!,  iv_product_image_layout)
                    } catch (e:IOException){
                        e.printStackTrace()
                    }
                }

            }
        }

    }



} // end of the class