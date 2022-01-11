package com.hardextech.store.ui.activities.ui.activities


import android.app.AlertDialog
import android.app.Dialog
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.hardextech.store.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialogue:Dialog
    private var doubleBackToExitPressedOnce = false

    fun showSnackBar(message: String, errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.jardsColor))
        } else {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        }
        snackBar.show()
    }

    fun showAlertDialogue(title: String, message: String, view: View?, positiveValueClickListener: View.OnClickListener){
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setPositiveButton("OK"){_,_->
                // what should happen when the positive button is clicked
                positiveValueClickListener.onClick(null)
            }
            .setMessage(message)
            .show()
    }

    fun showProgressDialogue(text:String){
        mProgressDialogue = Dialog(this)
      //  val inflate = (this as Activity).layoutInflater.inflate(R.layout.progress_dialogue, null)
        val view =LayoutInflater.from(this).inflate(R.layout.progress_dialogue, null)
        view.findViewById<TextView>(R.id.tvProgressDialogue).text = text
        mProgressDialogue.setContentView(view)
        mProgressDialogue.setCancelable(false)
        mProgressDialogue.setCanceledOnTouchOutside(false)
        mProgressDialogue.show()
    }

    fun dismissProgressDialogue(){
        mProgressDialogue.dismiss()
    }

    fun doubleClickToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, resources.getString(R.string.please_click_back_again_to_exist), Toast.LENGTH_SHORT).show()
        // set the timer for the splash screen
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false }, 2000) // End of the loop for threading
    }

    } // end of the onCreate method
