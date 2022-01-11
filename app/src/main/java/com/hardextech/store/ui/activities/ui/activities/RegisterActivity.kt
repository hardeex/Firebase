package com.hardextech.store.ui.activities.ui.activities


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hardextech.store.R
import com.hardextech.store.firestore.FirestoreClass
import com.hardextech.store.model.User


class RegisterActivity() : BaseActivity() {

    companion object{
        const val TAG =" RegisterActivity"

    }

    private lateinit var registerFirstName:TextInputLayout
    private lateinit var registerLastName:TextInputLayout
    private lateinit var registerEmail: TextInputLayout
    private lateinit var registerConfirmEmail: TextInputLayout
    private lateinit var registerPassword: TextInputLayout
    private lateinit var registerConfirmPassword: TextInputLayout
    private lateinit var cbTerms: AppCompatCheckBox
    private lateinit var btnRegister:Button
    private lateinit var tvRegisterHaveAccount:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // setting the action bar title
        supportActionBar?.title = resources.getString(R.string.create_an_account)
        // enabling the back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // setting the back arrow symbol
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        // changing the action color to the exact gradient at the bottomNav
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.fragment_bottom_color))

        // initializing the variables
        initiateVariables()


        tvRegisterHaveAccount.setOnClickListener {
            onBackPressed()
        }
        btnRegister.setOnClickListener {
            registerUser()
        }



    } // end of the onCreate method

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun initiateVariables() {
        registerFirstName=findViewById(R.id.registerFirstName)
        registerLastName=findViewById(R.id.registerLastName)
        registerEmail=findViewById(R.id.registerEmail)
        registerConfirmEmail=findViewById(R.id.registerConfirmEmail)
        registerPassword=findViewById(R.id.registerPassword)
        registerConfirmPassword=findViewById(R.id.registerConfirmPassword)
        cbTerms=findViewById(R.id.cbRegisterTerms)
        btnRegister=findViewById(R.id.btnRegister)
        tvRegisterHaveAccount=findViewById(R.id.tvHaveRegisterAccount)

    }

    // validating the user email address
    private fun validateEmail(email:String):Boolean{
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // validating the user details
    private fun validateUserDetails():Boolean{
        when{
            TextUtils.isEmpty(registerFirstName.editText?.text.toString().trim { it<=' ' })->{
                registerFirstName.error = resources.getString(R.string.first_name_error)
                return false
            }
            registerFirstName.editText?.text.toString().trim { it<=' ' }.length<=3->{
                registerFirstName.error = resources.getString(R.string.first_name_short)
                return false
            }
            TextUtils.isEmpty(registerLastName.editText?.text.toString().trim { it<=' ' })->{
                registerLastName.error = resources.getString(R.string.last_name_error)
                return false
            }
            registerLastName.editText?.text.toString().trim { it<=' ' }.length <=3->{
                registerLastName.error = resources.getString(R.string.last_name_short)
                return false
            }
            TextUtils.isEmpty((registerEmail.editText?.text.toString().trim { it<=' ' }))->{
                registerEmail.error = resources.getString(R.string.field_empty)
                return false
            }
            TextUtils.isEmpty(registerConfirmEmail.editText?.text.toString().trim { it<=' ' })->{
                registerConfirmEmail.error = "Confirm your email address"
                return false
            }
            registerConfirmEmail.editText?.text.toString().trim { it<=' ' } != registerEmail.editText?.text.toString().trim { it<=' ' }->{
                registerConfirmEmail.error = "Email do not match"
                return false
            }

            TextUtils.isEmpty(registerPassword.editText?.text.toString().trim { it<=' ' })->{
                registerPassword.error = resources.getString(R.string.enter_password)
                return false
            }
            registerPassword.editText?.text.toString().trim { it<=' ' }.length <8->{
                registerPassword.error = resources.getString(R.string.passworde_short)
                return false
            }
            TextUtils.isEmpty(registerConfirmPassword.editText?.text.toString().trim { it<=' ' })->{
                registerConfirmPassword.error = resources.getString(R.string.confirm_password)
                return false
            }
            registerConfirmPassword.editText?.text.toString().trim { it<=' ' }
                    != registerPassword.editText?.text.toString().trim { it<=' ' }->{
                registerConfirmPassword.error = resources.getString(R.string.password_mismatch)
                return false
            }
            !cbTerms.isChecked->{

                Toast.makeText(this, resources.getString(R.string.agree_terms) , Toast.LENGTH_SHORT).show()
                return false
            }

        }

        return true
    }

    // adding the user details to the database(Firebase)
    private fun registerUser(){
        if (validateUserDetails() && validateEmail(registerEmail.editText?.text.toString().trim { it<=' ' })){

            showProgressDialogue("Creating User Account")

            // converting the user email and password to string
            val email:String = registerEmail.editText?.text.toString().trim { it<=' ' }
            val password: String=  registerPassword.editText?.text.toString().trim { it<=' ' }


            // create an instance and user -- creating the user details in the Firebase Auth
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                // after the completion of creating the user email and password... then executes....
                .addOnCompleteListener(
                    OnCompleteListener <AuthResult> { task->

                        // when creating user details is successful
                        if (task.isSuccessful) {
                            Log.i(TAG, "New user Registration is successful")

                            // creating an identifier for each user
                            val user = Firebase.auth.currentUser
                            // sending confirmation link to the user
                            user!!.sendEmailVerification()

                                // after successful completion of sending the verification link
                                .addOnCompleteListener { sendVerificationLink->
                                    // if the sendVerificationLink registration is successful
                                    if (sendVerificationLink.isSuccessful){
                                        Log.i(TAG, " VERIFICATION LINK SENT SUCCESSFUL")
                                        Toast.makeText(this, "Verification Link sent to $registerEmail", Toast.LENGTH_LONG).show()
                                    } else{
                                        // if the verification link failed to send
                                        Log.e(TAG, "SendEmailVerification", task.exception)
                                        Toast.makeText(this, sendVerificationLink.exception?.message, Toast.LENGTH_LONG).show()
                                    }
                                    // the code for when confirmation link was sent successfully
                                    Toast.makeText(this, "Check ${registerEmail.editText?.text.toString()} for verification link and then LOGIN",
                                        Toast.LENGTH_LONG ).show()
                                } // end of verification link code

                            // Firebase to register the user
                            val firebaseUser = task.result!!.user!!

                            // storing the user details
                            val userDetails = User(
                                firebaseUser.uid,
                                registerFirstName.editText?.text.toString().trim { it<=' ' },
                                registerLastName.editText?.text.toString().trim { it<=' ' },
                                registerEmail.editText?.text.toString().trim { it<=' ' }
                            )

                            // registering the user in the cloud
                            FirestoreClass().registerUser(this, userDetails)
                            // to sign the user out of the register activity to the login activity if the registration is successful
                            FirebaseAuth.getInstance().signOut()
                            finish()

                        }else{
                            dismissProgressDialogue()
                            // if the user registration is not successful
                            Log.e(TAG, "Registration NOT successful")
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_LONG)
                                .show()
                        }

                }) // end of the complete listener for the register()
        } // end of the registerUser() if statement
    } // end of registerUser method

    fun userRegistrationSuccessful(){
        dismissProgressDialogue()
        Toast.makeText(this, "Your Registration is Successful", Toast.LENGTH_SHORT).show()
    }
} // end of the class