package com.hardextech.store.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import com.hardextech.store.R


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        hideActionBar()



        // set the timer for the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2500) // End of the loop for threading
    } // end of the onCreate method

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