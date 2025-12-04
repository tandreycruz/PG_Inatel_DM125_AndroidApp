package com.taibe.mytasks.extension

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.value(): String {
    return this.text.toString()
}

fun TextInputEditText.hasValue(): Boolean {
    return this.text?.isNotBlank() == true
}