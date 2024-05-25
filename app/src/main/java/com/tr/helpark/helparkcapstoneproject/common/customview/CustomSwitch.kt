package com.tr.helpark.helparkcapstoneproject.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.CustomSwitchBinding

class CustomSwitch @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding =
        CustomSwitchBinding.inflate(LayoutInflater.from(context), this, true)

    private var selectedTextView: TextView? = null

    private var onLoginTypeChangeListener: OnLoginTypeChangeListener? = null

    init {
        setInitialLanguage()

        binding.tvStaff.setOnClickListener {
            handleTextViewClick(binding.tvStaff)
        }

        binding.tvOperational.setOnClickListener {
            handleTextViewClick(binding.tvOperational)
        }
    }

    fun setOnLanguageChangeListener(listener: OnLoginTypeChangeListener) {
        this.onLoginTypeChangeListener = listener
    }

    private fun handleTextViewClick(textView: TextView) {
        // The clicked language is already selected, no need to do anything
        if (textView == selectedTextView) return

        selectedTextView?.setBackgroundResource(R.drawable.bg_custom_switch)
        textView.setBackgroundResource(R.drawable.bg_switch_selected)
        selectedTextView = textView

        /*val loginType = when (textView) {
            binding.tvOperational -> LoginType.OPERATIONAL
            binding.tvStaff -> LoginType.STAFF
            else -> LoginType.STAFF
        }*/

        //onLoginTypeChangeListener?.onLoginTypeChanged(loginType)
    }

    private fun setInitialLanguage() {
        handleTextViewClick(binding.tvStaff)
    }
}

interface OnLoginTypeChangeListener {
    fun onLoginTypeChanged(loginType: String)
}