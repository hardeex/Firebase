package com.hardextech.store.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@kotlinx.parcelize.Parcelize
data class Cart(
    val user_id: String = "",
    val product_id: String = "",
    val product_title: String = "",
    val product_image: String = "",
    val product_price: String = "",
    var cart_quantity: String = "",
    var product_quantity: String ="",
   // var product_quantity: Int = 0,
    var id: String = ""
): Parcelable
