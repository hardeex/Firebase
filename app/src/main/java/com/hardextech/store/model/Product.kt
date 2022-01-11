package com.hardextech.store.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val user_id: String = "",
    var id: String = "",
    val product_image: String = "",
    val product_title: String = "",
    val product_price: String = "",
    val product_description: String = "",
    val product_quantity: String = "",
   // val product_quantity: Int = 0,
    val user_name: String = "",
    var product_id:String =""

): Parcelable
