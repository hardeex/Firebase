package com.hardextech.store.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText


class MyCustomEditText (context: Context, attributeSet: AttributeSet): AppCompatEditText( context,  attributeSet){
    init {
        // initiate the font
        myCustomFont()
    }

    private fun myCustomFont() {
        // get the file from the asset folder and set it to the textView
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Sf_pro_display_black.otf")
        setTypeface(typeface)

    }
}