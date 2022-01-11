package com.hardextech.store.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.sql.Struct

object Constants {
    const val READ_EXTERNAL_STORAGE_CODE: Int= 1994
    const val PICK_IMAGE_REQUEST_CODE = 4991

    // The collections in the Firestore -- user abd products
    const val USERS: String = "Users"
    const val PRODUCTS: String = "Products"

    const val JDD_STORE_PREFERENCE: String = "MyStorePreferences"
    const val LOGGED_IN_USER: String = "LoggedInUser"
    const val PRODUCT_SHARED_PREFERENCE= "productSharedPreference"
    const val PRODUCT_LOGGED_IN ="product_loggedIn"
    const val EXTRA_USER_DETAILS = "extraUserDetails"

    const val FIRST_NAME:String = "firstName"
    const val LAST_NAME:String = "lastName"
    const val MALE: String ="Male"
    const val FEMALE: String ="Female"
    const val USER_MOBILE_NUMBER: String ="mobileNumber"
    const val GENDER: String ="gender"
    const val USER_DELIVERY_ADDRESS: String ="userDeliverContact"
    const val USER_PROFILE_IMAGE: String = "image"
    const val USER_PROFILE_COMPLETE: String = "profileCompletion"

    const val PRODUCT_IMAGE: String = "Product_Image"

    const val USER_ID:String ="user_id"
    const val EXTRA_PRODUCT_ID = "extra_product_id"
    const val DASHBOARD_EXTRA_PRODUCT_ID ="dashboard_extra_product_id"
    const val EDIT_EXTRA_PRODUCT_ID = "editExtraProductID"
    const val EDIT_PRODUCT_DETAILS: String = "edit_product_id"
    const val EXTRA_PRODUCT_OWNER_ID: String = "extra_product_owner_id"

    const val DEFAULT_CART_QUANTITY: String = "1"
    const val CART_QUANTITY: String = "cart_quantity"
    const val CART_ITEMS_COLLECTION: String = "CartItems"
    const val PRODUCT_ID: String = "product_id"


    // accessing the user gallery to select profile image
    fun showImageChooser(activity: Activity){
        // launching the image gallery from the user device
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    // Getting the file extension from the Firebase Storage
    fun getFIleExtension(activity: Activity, uri: Uri?): String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }


} // end of the class