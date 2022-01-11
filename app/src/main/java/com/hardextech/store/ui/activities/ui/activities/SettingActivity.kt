package com.hardextech.store.ui.activities.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.hardextech.store.R
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.User
import com.hardextech.store.utils.Constants
import com.hardextech.store.utils.GlideLoader

class SettingActivity : BaseActivity(), View.OnClickListener {

    private lateinit var editUserDetails_settings: TextView
    private  lateinit var settings_userProfileImage: ImageView
    private lateinit var toolbar_setting: androidx.appcompat.widget.Toolbar
    private lateinit var settings_user_fullname: TextView
    private lateinit var settings_user_email: TextView
    private lateinit var settings_user_gender: TextView
    private lateinit var settings_user_mobileNumber: TextView
    private lateinit var settings_user_deliveryAddress: TextView
    private lateinit var settings_back_arrow:ImageView
    private lateinit var setting_btnLogout: Button


    private lateinit var mUserDetails:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        hideActionBar()
        initializeVariable()

        settings_back_arrow.setOnClickListener (this)
        setting_btnLogout.setOnClickListener(this)
        editUserDetails_settings.setOnClickListener(this)

    } // end of the onCreate method

    private fun initializeVariable() {
        settings_userProfileImage=findViewById(R.id.settings_userProfileIPicture)
        toolbar_setting=findViewById(R.id.tool_settings_activity)
        settings_user_fullname=findViewById(R.id.setting_fullName)
        settings_user_email=findViewById(R.id.setting_email)
        settings_user_gender=findViewById(R.id.setting_gender)
        settings_user_mobileNumber=findViewById(R.id.setting_mobileNumber)
        settings_user_deliveryAddress=findViewById(R.id.setting_deliveryAddress)
        settings_back_arrow = findViewById(R.id.setting_back_arrow)
        setting_btnLogout = findViewById(R.id.btnLogout)
        editUserDetails_settings = findViewById(R.id.tvEditUserDetails_settings)
    }


    private fun hideActionBar() {

        // removing the title bar in different android device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // hiding the action bar
            supportActionBar?.hide()
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            // hiding the action bar
            supportActionBar?.hide()
        }
    }



    private fun getUserDetails(){
        showProgressDialogue("Retrieving Data...")
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsRetrievedSuccessfully(user: User?){

        mUserDetails = user!!
        dismissProgressDialogue()
        GlideLoader(this).loadUserImage(user.image, settings_userProfileImage)

        // passing the user details to the textView in the setting activity
         settings_user_fullname.text = "${user.lastName} ${user.firstName}"
         settings_user_gender.text = user.gender
         settings_user_email.text= user.email
         settings_user_mobileNumber.text = user.mobileNumber.toString()
         settings_user_deliveryAddress.text = user.userDeliverContact
    }

    // when this activity is active or open----- upon onResumed mode of the activity
    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){

                R.id.setting_back_arrow->{
                    onBackPressed()
                }

                R.id.btnLogout->{
                    // sign out the user
                    FirebaseAuth.getInstance().signOut()
                    // navigate the user to another activity
                    val intent = Intent(this, LoginActivity::class.java)
                    // clear all open activities and data
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    // start the intent
                    startActivity(intent)
                    // avoid onBackPressed
                    finish()
                }

                R.id.tvEditUserDetails_settings->{
                    startActivity(Intent(this, UserProfileActivity::class.java)
                        .putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    )
                }
            }
        }
    }
} //end of the class