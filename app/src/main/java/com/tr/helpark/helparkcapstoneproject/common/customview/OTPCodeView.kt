package com.tr.helpark.helparkcapstoneproject.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.OtpCodeItemBinding

class OTPCodeView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding = OtpCodeItemBinding.inflate(LayoutInflater.from(context), this, false)
    private val otpText1 = binding.edtOtp1
    private val otpText2 = binding.edtOtp2
    private val otpText3 = binding.edtOtp3
    private val otpText4 = binding.edtOtp4
    private val otpText5 = binding.edtOtp5
    private val otpText6 = binding.edtOtp6


    private var isVerifyCodeLengthListener: ((Boolean) -> Unit)? = null

    fun isVerifyCodeLengthListener(listener: (Boolean) -> Unit) {
        isVerifyCodeLengthListener = listener
    }


    init {
        addView(binding.root)
        initFocus()
        setListener()
    }

    private fun initFocus() {
        otpText1.isEnabled = true
        otpText1.postDelayed({
            otpText1.requestFocus()
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(otpText1, InputMethodManager.SHOW_FORCED)
        }, 500)
        isVerifyCodeLengthListener?.invoke(false)
    }

    private fun reset() {
        otpText1.isEnabled = false
        otpText2.isEnabled = false
        otpText3.isEnabled = false
        otpText4.isEnabled = false
        otpText5.isEnabled = false
        otpText6.isEnabled = false
        otpText1.setText("")
        otpText2.setText("")
        otpText3.setText("")
        otpText4.setText("")
        otpText5.setText("")
        otpText6.setText("")
        initFocus()
        isVerifyCodeLengthListener?.invoke(false)
    }

    private fun setTextChangeListener(
        fromEditText: EditText,
        targetEditText: EditText? = null,
        done: (() -> Unit)? = null,
    ) {
        fromEditText.addTextChangedListener {
            it?.let { string ->
                if (string.isNotEmpty()) {
                    targetEditText?.let { editText ->
                        editText.isEnabled = true
                        editText.requestFocus()
                    } ?: run {
                        done?.let { done ->
                            done()
                        }
                    }
                    fromEditText.clearFocus()
                    fromEditText.isEnabled = false
                    if (fromEditText == binding.edtOtp6) otpText6.isEnabled = true
                }
                fromEditText.setBackgroundResource(if (string.isNotEmpty()) R.drawable.bg_otp_edittext_focus else R.drawable.bg_otp_edittext_simple)
            }
        }
    }

    private fun setKeyListener(fromEditText: EditText, backToEditText: EditText) {
        fromEditText.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN) {
                if(fromEditText == binding.edtOtp6) {
                    isVerifyCodeLengthListener?.invoke(false)
                }
                if (fromEditText.text.isEmpty()) {
                    backToEditText.isEnabled = true
                    backToEditText.requestFocus()
                    backToEditText.setText("")
                    fromEditText.clearFocus()
                    fromEditText.isEnabled = false
                    return@setOnKeyListener true
                } else {
                    fromEditText.setText("")
                    return@setOnKeyListener true
                }
            }
            false
        }
    }


    fun setError(isError: Boolean) {
        if (isError) {
            otpText1.setBackgroundResource(R.drawable.bg_otp_edittext_error)
            otpText2.setBackgroundResource(R.drawable.bg_otp_edittext_error)
            otpText3.setBackgroundResource(R.drawable.bg_otp_edittext_error)
            otpText4.setBackgroundResource(R.drawable.bg_otp_edittext_error)
            otpText5.setBackgroundResource(R.drawable.bg_otp_edittext_error)
            otpText6.setBackgroundResource(R.drawable.bg_otp_edittext_error)
            otpText6.isEnabled = true
        } else {
            reset()
        }
    }


    fun getOtpCode(): String {
        return buildString {
            append(otpText1.text.toString().trim())
            append(otpText2.text.toString().trim())
            append(otpText3.text.toString().trim())
            append(otpText4.text.toString().trim())
            append(otpText5.text.toString().trim())
            append(otpText6.text.toString().trim())
        }
    }


    private fun setListener() {
        binding.root.setOnClickListener {
            val inputManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }

        otpText1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                otpText1.setBackgroundResource(R.drawable.bg_otp_edittext_focus)
            } else {
                otpText1.setBackgroundResource(if (otpText1.text.isNotEmpty()) R.drawable.bg_otp_edittext_focus else R.drawable.bg_otp_edittext_simple)
            }
        }

        otpText2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                otpText2.setBackgroundResource(R.drawable.bg_otp_edittext_focus)
            } else {
                otpText2.setBackgroundResource(if (otpText2.text.isNotEmpty()) R.drawable.bg_otp_edittext_focus else R.drawable.bg_otp_edittext_simple)
            }
        }

        otpText3.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                otpText3.setBackgroundResource(R.drawable.bg_otp_edittext_focus)
            } else {
                otpText3.setBackgroundResource(if (otpText3.text.isNotEmpty()) R.drawable.bg_otp_edittext_focus else R.drawable.bg_otp_edittext_simple)
            }
        }

        otpText4.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                otpText4.setBackgroundResource(R.drawable.bg_otp_edittext_focus)
            } else {
                otpText4.setBackgroundResource(if (otpText4.text.isNotEmpty()) R.drawable.bg_otp_edittext_focus else R.drawable.bg_otp_edittext_simple)
            }
        }
        otpText5.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                otpText5.setBackgroundResource(R.drawable.bg_otp_edittext_focus)
            } else {
                otpText5.setBackgroundResource(if (otpText5.text.isNotEmpty()) R.drawable.bg_otp_edittext_focus else R.drawable.bg_otp_edittext_simple)
            }
        }
        otpText6.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                otpText6.setBackgroundResource(R.drawable.bg_otp_edittext_focus)
            } else {
                otpText6.setBackgroundResource(if (otpText6.text.isNotEmpty()) R.drawable.bg_otp_edittext_focus else R.drawable.bg_otp_edittext_simple)
            }
        }

        setTextChangeListener(fromEditText = otpText1, targetEditText = otpText2)
        setTextChangeListener(fromEditText = otpText2, targetEditText = otpText3)
        setTextChangeListener(fromEditText = otpText3, targetEditText = otpText4)
        setTextChangeListener(fromEditText = otpText4, targetEditText = otpText5)
        setTextChangeListener(fromEditText = otpText5, targetEditText = otpText6)
        setTextChangeListener(fromEditText = otpText6, done = {
            isVerifyCodeLengthListener?.invoke(true)
        })

        setKeyListener(fromEditText = otpText2, backToEditText = otpText1)
        setKeyListener(fromEditText = otpText3, backToEditText = otpText2)
        setKeyListener(fromEditText = otpText4, backToEditText = otpText3)
        setKeyListener(fromEditText = otpText5, backToEditText = otpText4)
        setKeyListener(fromEditText = otpText6, backToEditText = otpText5)
    }


}