package com.hardextech.store.ui.activities.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.hardextech.store.R
import com.hardextech.store.utils.Constants

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // changing the action color to the exact gradient at the bottomNav
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.fragment_bottom_color))

        val sharedPreference = getSharedPreferences(Constants.JDD_STORE_PREFERENCE, Context.MODE_PRIVATE)
        val currentLoggedInUser = sharedPreference.getString(Constants.LOGGED_IN_USER, "Field is empty")
        Toast.makeText(this@MainActivity, currentLoggedInUser, Toast.LENGTH_LONG).show()
    }
}
