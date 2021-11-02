package com.hardextech.store.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hardextech.store.R

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // setting the action bar title
        supportActionBar?.title = "PROFILE"
        // enabling the back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // setting the back arrow symbol
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

    }
}