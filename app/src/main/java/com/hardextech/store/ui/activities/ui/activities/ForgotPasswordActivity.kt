package com.hardextech.store.ui.activities.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.hardextech.store.R

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var ivbackArrow:ImageView
    private lateinit var forgotPsdEmail: TextInputLayout
    private lateinit var btnForgotPsd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        hideActionBar()
        initiateVariables()

        supportActionBar?.hide()

        ivbackArrow.setOnClickListener { onBackPressed() }
        // when the user clicks on the forgot password button
        btnForgotPsd.setOnClickListener {
            val email = forgotPsdEmail.editText?.text.toString().trim { it<=' ' }
            if (email.isEmpty()){
                forgotPsdEmail.error = "Field can not be empty"
                return@setOnClickListener
            }else{
                showProgressDialogue("Sending Link....")
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task->
                        dismissProgressDialogue()
                        if (task.isSuccessful){
                            //TODO: change the Forebase text for password reset
                            Toast.makeText(this, "Email sent successfully to $email for resetting your password...", Toast.LENGTH_LONG).show()
                            finish()
                        } else{
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }

            }
        }

    } // end of the onCreate method

    private fun initiateVariables() {
        ivbackArrow= findViewById(R.id.ivBackArrow)
        forgotPsdEmail=findViewById(R.id.ForgotPasswordEmail)
        btnForgotPsd=findViewById(R.id.btnForgotPassword)
    }


    private fun hideActionBar() {
        // removing the title bar in different android device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            // hiding the action bar
            supportActionBar?.hide()
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else{
            // hiding the action bar
            supportActionBar?.hide()
        }
    }


} // end of the class