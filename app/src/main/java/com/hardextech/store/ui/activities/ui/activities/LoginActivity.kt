package com.hardextech.store.ui.activities.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hardextech.store.R
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.User
import com.hardextech.store.utils.Constants

class LoginActivity : BaseActivity(), View.OnClickListener {

    companion object{
        private const val TAG = "LoginActivity"
    }
    private lateinit var loginEmail: TextInputLayout
    private lateinit var loginPassword: TextInputLayout
    private lateinit var loginForgotPsd: Button
    private lateinit var btnLogin: Button
    private lateinit var tvHaveAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        hideActionBar()
        initiateVariables()

        // initiate the onClickListener from the activity
        loginForgotPsd.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        tvHaveAccount.setOnClickListener(this)
        // end of initiating the onClickListener from the activity

    } // end of the onCreate Method

    private fun initiateVariables() {
        loginEmail = findViewById(R.id.loginEmail)
        loginPassword = findViewById(R.id.loginPassword)
        loginForgotPsd = findViewById(R.id.btnForgotPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvHaveAccount = findViewById(R.id.tvHaveAnAccount)
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


    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(loginEmail.editText?.text.toString().trim { it <= ' ' }) -> {
                loginEmail.error = "Please, enter your registered email address"
                false
            }
            TextUtils.isEmpty(loginPassword.editText?.text.toString().trim { it <= ' ' }) -> {
                loginPassword.error = "Password must be filled"
                false
            }
            else -> {
                true
            } // end of the else statement

        } // end of the when statement
    } // end of the validateLoginDetails function

    private  fun loginUser(){
        if (validateLoginDetails()) {
            // show progress bar to the user
            showProgressDialogue("Logging in....")


            // collect the user details, convert to string and trim
            val email = loginEmail.editText?.text.toString().trim { it <= ' ' }
            val password = loginPassword.editText?.text.toString().trim { it <= ' ' }

            // check the user details from the Firebase Console for logging in feedback
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                // if successfully
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        // creating an identifier for each user
                            val user = Firebase.auth.currentUser
                        // when there is a user i.e the list of user is not zero or empty
                        if (user!=null){
                            // checking if the user has verify the register email by clicking on the confirmation link sent to the user registered email
                            val userEmailIsVerify = user.isEmailVerified
                            // when the registered email is verify
                            if (userEmailIsVerify){
                                //TODO: change the confirmation link text
                                FirestoreClass().getUserDetails(this@LoginActivity)
                                Toast.makeText(this@LoginActivity, "Welcome ", Toast.LENGTH_SHORT).show()

                            } else{
                                dismissProgressDialogue()
                                //  when the user try to sign in without verifying the registered email via the confirmation link
                                Toast.makeText(this,
                                    "Un-verify email account, check ${loginEmail.editText?.text.toString()} for confirmation link",
                                    Toast.LENGTH_LONG).show()
                            }

                        } else{
                            // when the user is null
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        }


                    } // end of the if statement
                    else {
                        dismissProgressDialogue()
                        // when the task is not successful
                        Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                }
        }
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.btnForgotPassword->{
                    startActivity(Intent(this, ForgotPasswordActivity::class.java))

                }

                R.id.btnLogin->{
                    loginUser()
                }

                R.id.tvHaveAnAccount->{
                    startActivity(Intent(this, RegisterActivity::class.java))
                }

            }
        }
    }

    fun userLoggedInSuccessfully(user: User?) {
        dismissProgressDialogue()
        if (user != null) {
            Log.i("FirstName: ", user.firstName)
        }
        if (user != null) {
            Log.i("FirstName: ", user.lastName)
        }
        if (user != null) {
            Log.i("Email: ", user.email)
        }
        if (user != null) {
            if (user.profileCompletion==0){
                startActivity(Intent(this@LoginActivity, UserProfileActivity::class.java)
                    .putExtra(Constants.EXTRA_USER_DETAILS, user))
            }else{
                startActivity(Intent(this, DashboardActivity::class.java))
            }
        }
        finish()
    }



}// end of the class
