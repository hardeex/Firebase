package com.hardextech.store.firestore


import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.hardextech.store.model.Cart
import com.hardextech.store.model.Product
import com.hardextech.store.model.User
import com.hardextech.store.ui.activities.ui.activities.*
import com.hardextech.store.ui.activities.ui.fragments.DashboardFragment
import com.hardextech.store.ui.activities.ui.fragments.ProductsFragment
import com.hardextech.store.utils.Constants


class FirestoreClass {
    // Create the instance for the Firestore
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo:User){
        //create the user collection
        mFireStore.collection(Constants.USERS)
        // create the user document field in the Firestore
            .document(userInfo.email)
        // merge all collected data about the user
            .set(userInfo, SetOptions.merge())
        // when the process is successful to this point
            .addOnSuccessListener {
                // call the userRegistrationSuccessful function
                activity.userRegistrationSuccessful()
            } // end of the onSuccessListener
        // if the process is not successful
            .addOnFailureListener { e->
                activity.dismissProgressDialogue()
                Log.e(activity.javaClass.simpleName, "Error while registering the user", e)

            } // end of the failure listener
    } // end of the registerUser

     fun getCurrentUserID(): String{
        // create an instance of the FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        // assigning a value to the currentUser when null
        var currentUserID = ""
        if (currentUser!=null){
            currentUserID = currentUser.email.toString()
        }
             return currentUserID

    } //end of the getCurrentUserID()

    fun  getUserDetails(activity: Activity){
        // passing the collection from which I wants the data and the documentId to get the field of the user
        mFireStore.collection(Constants.USERS).document(getCurrentUserID()).get()
            .addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName, document.toString())
                // converting the document to an object
                val userDocument = document.toObject(User::class.java)
                // storing the user details on the user device and ensuring other apps on the user device can not access the data
                val sharedPreferences = activity.getSharedPreferences(Constants.JDD_STORE_PREFERENCE, Context.MODE_PRIVATE)
                /*
                 enabling an editor in order to save data on the sharedPreferences
                 KEY: Constants.USER_LOGGED_IN
                 VALUE: ${userDocument?.firstName}, ${userDocument?.lastName}
                 */
                val editor = sharedPreferences.edit()
                if (userDocument != null) {
                    editor.putString(Constants.LOGGED_IN_USER,
                        " Full Name: ${userDocument.lastName} ${userDocument.firstName}\n Email: ${userDocument.email}")
                }
                editor.apply()

                when(activity){
                    is LoginActivity ->{
                            activity.userLoggedInSuccessfully(userDocument)
                    }

                    is SettingActivity->{
                        activity.userDetailsRetrievedSuccessfully(userDocument)
                    }
                } // end of the when statement
            }// end of the successListener
            .addOnFailureListener {e->
                when (activity){
                    is LoginActivity ->{
                        activity.dismissProgressDialogue()
                    }

                    is SettingActivity->{
                        activity.dismissProgressDialogue()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error while getting user details", e)
            }
    } // end of the getUserDetails()


//    fun  getProductDetailsFromProductDetailActivity(activity: Activity){
//        // passing the collection from which I wants the data and the documentId to get the field of the user
//        mFireStore.collection(Constants.PRODUCTS)
//            .document(getCurrentUserID())
//            .get()
//
//            .addOnSuccessListener { document->
//                Log.i(activity.javaClass.simpleName, document.toString())
//                // converting the document to an object
//                val productDetails = document.toObject(Product::class.java)
//
//                when(activity){
//                    is ProductDetailsActivity ->{
//                        activity.productDetailsInfoPassedSuccessfully(productDetails)
//                    }
//
//                    is AddProductActivity->{
//                     //   activity.userDetailsRetrievedSuccessfully(userDocument)
//                    }
//                } // end of the when statement
//            }// end of the successListener
//            .addOnFailureListener {e->
//                when (activity){
//                    is ProductDetailsActivity ->{
//                        activity.dismissProgressDialogue()
//                    }
//
//                    is AddProductActivity->{
//                        activity.dismissProgressDialogue()
//                    }
//                }
//                Log.e(activity.javaClass.simpleName, "Error while getting user details", e)
//            }
//    } // end of the getUserDetails()



    fun updateUserDetails(activity: Activity, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)


            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity ->{
                        activity.successfullyUpdatedUserDetails()
                    }
                } // end of the when statement
            }
            .addOnFailureListener { e->
                when(activity){
                    is UserProfileActivity ->{
                        activity.dismissProgressDialogue()
                    }
                } // end of the when statement
                Log.e(activity.javaClass.simpleName, "Error updating the user details", e)
            }
    }

    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product){
        mFireStore.collection(Constants.PRODUCTS).document().set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                // called method when the product uploading is successful
                activity.productUploadSuccessfully()
            }

            .addOnFailureListener { e->
                // when the product upload failed
                activity.dismissProgressDialogue()
                Log.e(activity.javaClass.simpleName, "Error updating the product details", e)

            }

    }  // end of uploadProductDetails method

    fun getProductList(fragment:Fragment){
        mFireStore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID()).get()

            .addOnSuccessListener { documents->
                Log.i("Product List", documents.documents.toString())
                val productLists: ArrayList<Product> = ArrayList()
                // loop through the product list
                for (i in documents.documents){
                    val products = i.toObject(Product::class.java)
                    products!!.product_id = i.id
                    productLists.add(products)
                }

                when(fragment){
                    is ProductsFragment->{
                        fragment.successfullyUploadedProductListFromFirestore(productLists)
                    }
                }
            } // end of addOnSuccessListener
    }

    fun  getDashboardItemList(fragment:DashboardFragment){
        mFireStore.collection(Constants.PRODUCTS).get()

            // if the task is successful
            .addOnSuccessListener { documents->
                Log.i(fragment.javaClass.simpleName, documents.documents.toString())
                val productList: ArrayList<Product> = ArrayList()
                for (i in documents.documents){
                    val products = i.toObject(Product::class.java)!!
                    products.product_id = i.id
                    productList.add(products)
                } // end of the for loop

                fragment.successfullyUploadedDashboardItemListFromFirestore(productList)

            }

            // if the operation failed
            .addOnFailureListener { exception->
                // dismiss/hide progressDialog
                fragment.dismissProgressDialogue()
                Log.e(fragment.javaClass.simpleName, "Error retrieving DashBoard Item List", exception)

            }
    } // end of getDashboardItemList



    /*
     uploading the images to the Firestore
     check if the uploaded image is a profile image or a product image ---- imageType: String
     */
    fun uploadImagesToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String){
        // first of all, Get the Firebase Storage Reference
        val storageReference = FirebaseStorage.getInstance().reference.child(imageType + System.currentTimeMillis()
                +"." + Constants.getFIleExtension(activity, imageFileURI))

        // Transferring the file online
        storageReference.putFile(imageFileURI!!)
            .addOnSuccessListener {taskSnapShot->
                // when the uploading is successful
                Log.i("Firebase Image URI", taskSnapShot.metadata!!.reference?.downloadUrl.toString())
                // Get the downloaded uri from the snapShot
                taskSnapShot.metadata!!.reference?.downloadUrl
                    ?.addOnSuccessListener { uri->
                        Log.i("Downloaded Image URI", uri.toString())
                        when(activity){
                            is UserProfileActivity ->{
                                activity.uploadImageSuccessfully(uri.toString())
                            }

                            is AddProductActivity ->{
                                activity.uploadProductImageSuccessfully(uri.toString())
                            }
                        }

                    }
            } // end of onSuccessListener

            .addOnFailureListener{exception->
                when(activity){
                    is UserProfileActivity ->{
                        activity.dismissProgressDialogue()
                    }

                    is AddProductActivity->{
                        activity.dismissProgressDialogue()
                    }
                } // end of the when statement

                Log.e(activity.javaClass.simpleName, exception.message.toString())
            }
    }

    fun getProductDetail(activity: Activity, product_id: String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(product_id)
            .get()

            .addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName, document.toString())
                val product = document.toObject(Product::class.java)

                when(activity){
                    is ProductDetailsActivity->{
                        if (product != null) {
                            activity.productDetailsRetrievedSuccessfully(product)
                        }
                    }

                    is DashboardProductDetailsActivity->{
                        if (product != null) {
                            activity.productDetailsRetrievedSuccessfully(product)
                        }
                    }

                } // end of the when statement


            }

            .addOnFailureListener { e->
                when(activity){
                    is ProductDetailsActivity->{
                        activity.dismissProgressDialogue()
                        Log.e(activity.javaClass.simpleName, "Error retrieving the product details", e)
                    }

                    is DashboardProductDetailsActivity->{
                        activity.dismissProgressDialogue()
                        Log.e(activity.javaClass.simpleName, "Error retrieving the product details", e)
                    }

                }

            }
    }

    fun getProductInfo(activity: Activity) {
        mFireStore.collection(Constants.PRODUCTS)
            .document(getCurrentUserID())
            .get()

            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val product = document.toObject(Product::class.java)

//                val sharedPreferences = activity.getSharedPreferences(Constants.PRODUCT_SHARED_PREFERENCE, Context.MODE_PRIVATE)
//                val editor = sharedPreferences.edit()
//                if (product != null) {
//                    editor.putString(Constants.PRODUCT_LOGGED_IN,
//                        "PRODUCT NAME: ${product.product_title} \n PRODUCT PRICE: ${product.product_price}")
//                }
//                editor.apply()

                when (activity) {
                    is ProductDetailsActivity -> {
                        activity.productDetailsInfoPassedSuccessfully(product)
                    }
                }

//                is AddProductActivity->{
//
//            }
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error retrieving files", e)

                when(activity){
                    is ProductDetailsActivity->{
                        activity.dismissProgressDialogue()
                    }
                }


            }
    }


    fun getProductDetail(activity: ProductDetailsActivity, product_id: String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(product_id)
            .get()

            .addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName, document.toString())
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    activity.productDetailsRetrievedSuccessfully(product)
                }

            }

            .addOnFailureListener { e->
                activity.dismissProgressDialogue()
                Log.e(activity.javaClass.simpleName, "Error retrieving the product details", e)
            }
    }

    fun deleteProductFromCloudFirestore(productsFragment: ProductsFragment, product_id: String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(product_id)
            .delete()

            .addOnSuccessListener {
                productsFragment.successfullyDeletedProductFromFirestore()
            }

            .addOnFailureListener { e->
                productsFragment.dismissProgressDialogue()
                Log.e(productsFragment.requireActivity().javaClass.simpleName, "Error deleting product", e)
            }
    }

//    fun getProductDetailDashboardActivity(activity: DashboardProductDetailsActivity, product_id: String){
//        mFireStore.collection(Constants.PRODUCTS)
//            .document(product_id)
//            .get()
//
//            .addOnSuccessListener { document->
//                Log.i(activity.javaClass.simpleName, document.toString())
//                val product = document.toObject(Product::class.java)
//                if (product != null) {
//                    activity.productDetailsRetrievedSuccessfully(product)
//                }
//
//            }
//
//            .addOnFailureListener { e->
//                activity.dismissProgressDialogue()
//                Log.e(activity.javaClass.simpleName, "Error retrieving the product details", e)
//            }
//    }

    fun addCartItemsToFirestore(activity: DashboardProductDetailsActivity, addProductToCart:Cart){
    mFireStore.collection(Constants.CART_ITEMS_COLLECTION)
        .document()
        .set(addProductToCart, SetOptions.merge())

        .addOnSuccessListener {
            activity.productAddedToCartSuccessfully()
        }
        .addOnFailureListener { e->
            activity.dismissProgressDialogue()
            Log.e(activity.javaClass.simpleName, "Error while creating product cart documents", e)
        }
    }


    fun updateMyCartItem(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.CART_ITEMS_COLLECTION).document(cart_id).update(itemHashMap)

            .addOnSuccessListener {
                when(context){
                    is CartListActivity->{
                        context.updateCartItemSuccessfullyFromFirestore()
                    }
                }
            }
            .addOnFailureListener { e->
                when(context){
                    is CartListActivity->{
                        context.dismissProgressDialogue()
                    }
                }
                Log.e(context.javaClass.simpleName, "Error updating $cart_id from the cart list", e)
            }
    }
    fun checkIfProductExistInCart(activity: DashboardProductDetailsActivity, product_id: String){
        mFireStore.collection(Constants.CART_ITEMS_COLLECTION)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, product_id)
            .get()

            .addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                if (document.documents.size > 0){
                    activity.checkIfProductExistInCart()
                }else{
                    activity.dismissProgressDialogue()
                }
            }

            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName, "Error navigating to CART ", e)
                activity.dismissProgressDialogue()
            }
    }

    fun getCartList(activity: Activity){
        // the method for downloading the cart item list
        mFireStore.collection(Constants.CART_ITEMS_COLLECTION)
                // get the cart items for the logged in user
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()

            .addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                // get the list of the cart items--- number of products in the cart
                val listOfCartItems: ArrayList<Cart> = ArrayList()
                // run through each of the document(FireStore document field) in the list
                for (loopingThroughCartItems in document.documents){
                    // converting the products in the cart to an object and surround with null check
                    val cartItemListObject = loopingThroughCartItems.toObject(Cart::class.java)!!
                    cartItemListObject.id= loopingThroughCartItems.id
                    // add the result of the loop to the cart item arrayList
                    listOfCartItems.add(cartItemListObject)
                }

                when(activity){
                    is CartListActivity->{
                        activity.successfullyGetTheCartItemList(listOfCartItems)
                    }

                }
            }.addOnFailureListener {e->
                Log.e(activity.javaClass.simpleName, " Error downloading products in the cart", e)
                when(activity){
                    is CartListActivity->{
                        activity.dismissProgressDialogue()
                    }
                }
            }
    }

    fun getAllProductList(activity: CartListActivity){
        mFireStore.collection(Constants.PRODUCTS).get()

            .addOnSuccessListener { document->
                Log.i("Product Lists", document.documents.toString())
                val productList: ArrayList<Product> = ArrayList()
                // to get the product list, run a for loop through the documents
                for (everyProductInTheDocuments in document){
                    // convert the list of the product to an object
                    val product = everyProductInTheDocuments.toObject(Product::class.java)
                    // assigning the  product id
                    product.product_id = everyProductInTheDocuments.id
                    productList.add(Product())
                }

                activity.successfullyGetProductFromFirestore(productList)
            }
            .addOnFailureListener { e->
                activity.dismissProgressDialogue()
                Log.e("Get Product List", "Error getting the product list", e)
            }
    }

    fun deleteItemFromCart(context: Context, cart_id: String){
        mFireStore.collection(Constants.CART_ITEMS_COLLECTION).document(cart_id).delete()

            .addOnSuccessListener {
                when(context){
                    is CartListActivity->{
                        context.deleteCartItemSuccessfully()
                    }
                }

            }
            .addOnFailureListener { e->
                when(context){
                    is CartListActivity->{
                        context.dismissProgressDialogue()
                    }
                }
                Log.e(context.javaClass.simpleName, "Error deleting $cart_id from the cart list", e)

            }
    }






    }
