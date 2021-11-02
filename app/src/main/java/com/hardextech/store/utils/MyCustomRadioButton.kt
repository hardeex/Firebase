package com.hardextech.store.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class MyCustomRadioButton(context: Context, attributeSet: AttributeSet):AppCompatRadioButton(context, attributeSet) {
    init {
        applyRadioButtonFont()
    }

    private fun applyRadioButtonFont() {
        val typeface = Typeface.createFromAsset(context.assets, "Sf_pro_display_black.otf")
        setTypeface(typeface)
    }
}