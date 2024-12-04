package com.snackcheck.view.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.snackcheck.R

class CustomPasswordEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val view = rootView
                val textInputLayout = view.findViewById<TextInputLayout>(R.id.tl_password)
                if (s.toString().length < 8) {
                    textInputLayout?.error =
                        resources.getString(R.string.password_minimum_characters)
                } else {
                    textInputLayout?.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}

class CustomConfirmPasswordEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val view = rootView
                val textInputLayout = view.findViewById<TextInputLayout>(R.id.tl_password_confirmation)
                val passwordEditText = view.findViewById<CustomPasswordEditText>(R.id.ed_password)
                val password = passwordEditText.text.toString()
                if (s.toString() != password) {
                    textInputLayout?.error =
                        resources.getString(R.string.password_confirmation_error)
                } else {
                    textInputLayout?.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}