package com.hardextech.store.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hardextech.store.R
import java.io.IOException

class GlideLoader(val context: Context) {

    // loading the user profile image
    fun loadUserImage(userImage: Any, userImageView:ImageView){
        try {
            Glide.with(context)
                .load(userImage)
                .centerCrop()
                .placeholder(R.drawable.default_profile_picture)
                .into(userImageView)
        }catch (e:IOException){
            e.printStackTrace()
        }
        
    }

    fun loadProductImage(userImage: Any, userImageView:ImageView){
        try {
            Glide.with(context)
                .load(userImage)
                .centerCrop()
                .into(userImageView)
        }catch (e:IOException){
            e.printStackTrace()
        }

    }
}