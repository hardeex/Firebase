package com.hardextech.store.firestore


import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hardextech.store.activities.LoginActivity
import com.hardextech.store.activities.RegisterActivity
import com.hardextech.store.model.User
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

    private fun getCurrentUserID(): String{
        // create an instance of the FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        // assigning a value to the currentUser when null
        var currentUserID = ""
        if (currentUser!=null){
            currentUserID = currentUser.email.toString()
        }
        return currentUserID
    } //end of the getCurrentUserID()

    fun getUserDetails(activity: Activity){
        // passing the collection from which I wants the data and the documentId to get the field of the user
        mFireStore.collection(Constants.USERS).document(getCurrentUserID()).get()
            .addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName, document.toString())
                // converting the document to an object
                val userDocument = document.toObject(User::class.java)
                // storing the user details on the user device and ensuring other apps on the user device can not access the data
                val sharedPreferences = activity.getSharedPreferences(Constants.MY_STORE_PREFERENCES, Context.MODE_PRIVATE)
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
                    is LoginActivity->{
                            activity.userLoggedInSuccessfully(userDocument)
                    }
                } // end of the when statement
            }// end of the successListener
            .addOnFailureListener {e->
                when (activity){
                    is LoginActivity->{
                        activity.dismissProgressDialogue()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error while getting user details", e)
            }
    } // end of the getUserDetails()


} // end of the class