package com.hardextech.store.activities


import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.hardextech.store.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialogue:Dialog

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
    } // end of the onCreate method
