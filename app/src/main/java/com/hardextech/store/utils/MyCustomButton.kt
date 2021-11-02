package com.hardextech.store.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton


class MyCustomButton(context: Context, attributeSet: AttributeSet): AppCompatButton(context, attributeSet) {

    init {
        //apply the button font
        applyButtonFont()
    }

    private fun applyButtonFont() {
        val typeface= Typeface.createFromAsset(context.assets, "Sf_pro_display_black.otf")
        setTypeface(typeface)
    }
}