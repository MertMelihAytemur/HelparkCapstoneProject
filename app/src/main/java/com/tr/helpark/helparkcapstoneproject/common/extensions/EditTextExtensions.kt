package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.tr.helpark.helparkcapstoneproject.R

fun AppCompatEditText.setPhoneMaskWithListener(
    onPhoneCompleted: (Boolean) -> Unit,
    onPhoneTypeFaceWarning: ((Boolean) -> Unit)? = null,
) {
    addTextChangedListener(object : TextWatcher {
        private var isFormatting: Boolean = false
        private var cursorPosition: Int = 0
        private var beforeCursorPosition: Int = 0

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            beforeCursorPosition = start
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            cursorPosition = start + count
        }

        override fun afterTextChanged(s: Editable) {
            if (isFormatting) {
                return
            }

            isFormatting = true

            val formattedNumber = s.toString().replace("[^\\d]".toRegex(), "")

            val formattedStringBuilder = StringBuilder()

            var i = 0
            val size = formattedNumber.length

            while (i < size) {
                if (i == 0 && formattedNumber[i] != '5') {
                    onPhoneTypeFaceWarning?.invoke(true)
                } else {
                    onPhoneTypeFaceWarning?.invoke(false)
                    if (i == 3 || i == 6 || i == 8) {
                        formattedStringBuilder.append(' ')
                    }
                    formattedStringBuilder.append(formattedNumber[i])
                }
                i++
            }

            setText(formattedStringBuilder.toString())

            if (cursorPosition <= text.toString().length) {
                setSelection(cursorPosition)
            } else {
                setSelection(text.toString().length)
            }

            isFormatting = false

            if (formattedNumber.length == 10) {
                onPhoneCompleted.invoke(true)
            } else {
                onPhoneCompleted.invoke(false)
            }
        }
    })
}

/**
 * Move cursor to end of the edit text
 */
fun EditText.moveCursorToEnd() {
    Handler(Looper.getMainLooper()).postDelayed(
        {
            setSelection(text.length)
        },
        50
    )
}

/**
 * Request focus and show Keyboard
 */
fun EditText.showKeyboard() {
    post {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

/**
 * Set editText empty
 */
fun EditText.clear() {
    setText("")
}

/**
 * Change background of editText to error state whenever format validation failed
 */
fun EditText.setErrorState(errorMessage: String, tvError: TextView) {
    tag = "error"
    setBackgroundResource(R.drawable.et_error_background)
    tvError.visibility = View.VISIBLE
    tvError.text = errorMessage
}

/**
 * Change background of editText to normal state from error state
 */
fun EditText.inactiveErrorState(tvError: TextView) {
    tag = ""
    setBackgroundResource(R.drawable.rounded_rectangle_card_view_background)
    tvError.visibility = View.GONE
}

val EditText.isErrorStateActive
    get() = tag == "error"