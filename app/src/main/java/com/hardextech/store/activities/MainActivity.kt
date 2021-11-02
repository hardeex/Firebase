package com.hardextech.store.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.hardextech.store.R
import com.hardextech.store.utils.Constants

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPreference = getSharedPreferences(Constants.MY_STORE_PREFERENCES, Context.MODE_PRIVATE)
        val currentLoggedInUser = sharedPreference.getString(Constants.LOGGED_IN_USER, "Field is empty")
        Toast.makeText(this@MainActivity, currentLoggedInUser, Toast.LENGTH_LONG).show()
    }
}
