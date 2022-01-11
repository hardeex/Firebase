package com.hardextech.store.ui.activities.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.hardextech.store.R
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.User
import com.hardextech.store.utils.Constants
import com.hardextech.store.utils.GlideLoader
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var userProfileImage: ImageView
    private  lateinit var userFirstName: TextInputLayout
    private  lateinit var userLastName: TextInputLayout
    private  lateinit var userEmail: TextInputLayout
    private  lateinit var userPhoneNumber: TextInputLayout
    private  lateinit var userDeliveryAddress: TextInputLayout
    private lateinit var userGender: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var userProfile_submitButton: Button

    private lateinit var  mUserDetails: User
    private  var mSelectedImageURI: Uri? = null
    private var mUserProfileImageURI: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        supportActionBar?.title = resources.getString(R.string.user_profile_action_bar_title)

        initiateVariable()
        initializeViews()
        getRegisteredUserDetails()


    } // end of the onCreate method


    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this, LoginActivity::class.java))
        return super.onSupportNavigateUp()
    }


    private fun initializeViews() {
        userProfileImage.setOnClickListener (this@UserProfileActivity)
        userProfile_submitButton.setOnClickListener (this@UserProfileActivity)
    }

    private fun initiateVariable() {
        userProfileImage = findViewById(R.id.userImage)
        userFirstName =findViewById(R.id.userProfileFirstName)
        userLastName =findViewById(R.id.userProfileLastName)
        userEmail=findViewById(R.id.userProfileEmail)
        userPhoneNumber = findViewById(R.id.userProfilePhoneNumber)
        userDeliveryAddress = findViewById(R.id.userDeliveryAddress)
        userGender = findViewById(R.id.userGender)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        userProfile_submitButton= findViewById(R.id.userProfile_saveButton)
    }

    private fun validateUserDetails(): Boolean{
        val phoneNumber = userPhoneNumber.editText?.text.toString().trim { it<=' ' }
        val deliveryAddress = userDeliveryAddress.editText?.text.toString().trim { it<=' ' }
        when{
            TextUtils.isEmpty(phoneNumber)->{
                userPhoneNumber.error = "Please, enter mobile number"
                return false
            }
            userPhoneNumber.editText?.text.toString().trim { it<=' ' }.length < 6->{
                userPhoneNumber.error = "Enter a valid contact number"
                return false
            }
            TextUtils.isEmpty(deliveryAddress)->{
                userDeliveryAddress.error = "Field can not be empty"
                return false
            }
            else->{
                return true
            }
        }
    }

    private fun getRegisteredUserDetails() {
        // getting the passed parcelable data from the LoginActivity
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        userFirstName.editText?.setText(mUserDetails.firstName)
        userFirstName.isEnabled = false
        userLastName.editText?.setText(mUserDetails.lastName)
        userLastName.isEnabled = false
        userEmail.isEnabled = false
        userEmail.editText?.setText(mUserDetails.email)

        if (mUserDetails.profileCompletion == 0){
            supportActionBar?.title = resources.getString(R.string.user_profile_action_bar_title)
            // changing the action color to the exact gradient at the bottomNav
            supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this, R. drawable.fragment_bottom_color))

        } else{
            // setting the action bar title
            supportActionBar?.title = resources.getString(R.string.user_profile_edit_action_bar_title)
            // enabling the back arrow
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            // setting the back arrow symbol
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
            // changing the action color to the exact gradient at the bottomNav
            supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this, R. drawable.fragment_bottom_color))


            // loading the userProfile Image
            GlideLoader(this).loadUserImage(mUserDetails.image, userProfileImage)


            if (mUserDetails.mobileNumber !=0L){
                userPhoneNumber.editText?.setText(mUserDetails.mobileNumber.toString())
            }

            if (mUserDetails.userDeliverContact.isNotEmpty()){
                userDeliveryAddress.editText?.setText(mUserDetails.userDeliverContact)
            }

            if (mUserDetails.gender== Constants.MALE){
                rbMale.isChecked = true
            } else{
                rbFemale.isChecked = true
            }
        }
    }

     fun successfullyUpdatedUserDetails(){
        dismissProgressDialogue()
        Toast.makeText(this, "Update is successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    fun uploadImageSuccessfully(imageUri: String){
       // dismissProgressDialogue()
       // Toast.makeText(this, "Image uploaded successfully \n Path: $imageUri", Toast.LENGTH_LONG).show()
        mUserProfileImageURI = imageUri
        updateProfileDetails()
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){

                R.id.userImage->{
                    // first check for permission when clicked --- if permission is granted
                    if (ContextCompat.checkSelfPermission(this@UserProfileActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE ) ==PackageManager.PERMISSION_GRANTED){
                       // Toast.makeText(this@UserProfileActivity, "You already Granted Permission...", Toast.LENGTH_SHORT).show()
                        Constants.showImageChooser(this@UserProfileActivity)
                    } else{
                        // if permission was not granted --- Request Permission
                        ActivityCompat.requestPermissions(this@UserProfileActivity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_EXTERNAL_STORAGE_CODE)
                    }
                } // end of userImage id

                R.id.userProfile_saveButton->{
                    if (validateUserDetails()){
                        showProgressDialogue("Updating...")
                        if (mSelectedImageURI!=null){
                            FirestoreClass().uploadImagesToCloudStorage(this, mSelectedImageURI, Constants.USER_PROFILE_IMAGE)
                        } else{
                            // HashMap for updating the user details in the Firestore
                            updateProfileDetails()
                        }
                    } // end of the BIG if statement
                } // end of the userProfile_saveButton method
            } // end of the when statement for the views
        } // end of the onClick if statement
    } // end of the onClick function

    private fun updateProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val userFirstName = userFirstName.editText?.text.toString().trim { it<=' ' }
        if (userFirstName!=mUserDetails.firstName){
            userHashMap[Constants.FIRST_NAME] = userFirstName
        }

        val userLastName = userLastName.editText?.text.toString().trim { it<=' ' }
        if (userFirstName!=mUserDetails.lastName){
            userHashMap[Constants.LAST_NAME] = userLastName
        }

        val userMobileNumber = userPhoneNumber.editText?.text.toString().trim { it<=' ' }

        val userDeliveryPoint = userDeliveryAddress.editText?.text.toString().trim { it<=' ' }

        val gender = if (rbMale.isChecked){
            Constants.MALE
        } else{
            Constants.FEMALE
        }

        // pass the imageURI from the FireStore Storage to the Cloud Firestore
        userHashMap[Constants.USER_PROFILE_IMAGE] = mUserProfileImageURI

        // Updating the user details in the database
        if (userMobileNumber.isNotEmpty() && userMobileNumber != mUserDetails.mobileNumber.toString()){
            userHashMap[Constants.USER_MOBILE_NUMBER] = userMobileNumber.toLong()
        }


        userHashMap[Constants.USER_DELIVERY_ADDRESS] = userDeliveryPoint.toString()

        if (gender.isNotEmpty() && gender != mUserDetails.gender){
            userHashMap[Constants.GENDER] = gender
        }

        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.USER_PROFILE_COMPLETE] = 1

        FirestoreClass().updateUserDetails(this@UserProfileActivity, userHashMap)
    }

    override fun onBackPressed() {
      //  super.onBackPressed()
        doubleClickToExit()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==Constants.READ_EXTERNAL_STORAGE_CODE){
            // if permission is granted
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(this@UserProfileActivity, "The storage Permission Granted Successfully.", Toast.LENGTH_SHORT).show()
                Constants.showImageChooser(this)
            } else{
                // if permission was declined
                Toast.makeText(this@UserProfileActivity,
                    "Storage Permission required for uploading your photo/image. You can also allow from your device settings",
                    Toast.LENGTH_LONG).show()
            }
        }
    } // end of endRequestPermissionResult method

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK){
                if (requestCode==Constants.PICK_IMAGE_REQUEST_CODE){
                   if (data!=null){
                       try {
                          mSelectedImageURI = data.data
                           //userProfileImage.setImageURI((selectImageURI))
                           if (mSelectedImageURI != null) {
                               // TODO: ADD PICASO TO THE GLIDELOADER FR ALL IMAGES ON THIS APP

                               GlideLoader(this@UserProfileActivity).loadUserImage(mSelectedImageURI!!, userProfileImage)
                           }
                       } catch (e: IOException){
                           e.printStackTrace()
                           Toast.makeText(this, "Error selecting image", Toast.LENGTH_SHORT).show()
                       }
                   }

                }
            }

    }


} // end of the class