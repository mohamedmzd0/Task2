package com.appsquare.task.helper

import android.util.Patterns
import com.appsquare.task.R
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Matcher
import java.util.regex.Pattern

fun TextInputEditText.isNameValid(): Boolean {
    if (this.text.toString().trim().isEmpty()) {
        this.error = this.context.resources.getString(R.string.required)
        return false
    }
    if (this.text.toString().trim().length < 3) {
        this.error = this.context.resources.getString(R.string.name_must_3_characters)
        return false
    }
    return true
}

fun TextInputEditText.isEditTextValid(): Boolean {
    if (this.text.toString().trim().isEmpty()) {
        this.error = this.context.resources.getString(R.string.required)
        return false
    }
    return true
}

fun TextInputEditText.isNumberValid(): Boolean {
    if (this.text.toString().trim().isEmpty()) {
        this.error = this.context.resources.getString(R.string.required)
        return false
    }
    if (!(this.text.toString().trim().toInt() > 0)) {
        this.error = this.context.resources.getString(R.string.invalid_number)
        return false
    }
    return true
}

fun TextInputEditText.isEmailValid(): Boolean {
    if (!this.isEditTextValid())
        return false
    if (
        !Patterns.EMAIL_ADDRESS.matcher(this.text.toString().trim()).matches()
    ) {
        this.error = this.context.resources.getString(R.string.email_not_valid)
        return false
    }
    return true
}

fun TextInputEditText.isPasswordalid(): Boolean {
    if (!this.isEditTextValid())
        return false
    val matcher: Matcher

    val pattern: Pattern = Pattern.compile(".{8,30}")
    matcher = pattern.matcher(this.text.toString())
    if (!matcher.matches()) {
        this.error = this.context.resources.getString(R.string.password_weak)
        return false
    }
    return true
}