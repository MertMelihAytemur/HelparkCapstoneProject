package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText

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