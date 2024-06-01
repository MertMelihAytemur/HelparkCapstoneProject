package com.tr.helpark.helparkcapstoneproject.common.customview

import android.content.Context
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.isErrorStateActive
import com.tr.helpark.helparkcapstoneproject.databinding.ApEditTextViewLayoutBinding
import kotlinx.android.parcel.Parcelize

@SuppressWarnings("TooManyFunctions")
class ApEditTextCreditCard : LinearLayout {

    private lateinit var binding: ApEditTextViewLayoutBinding

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(attributeSet)
        binding.etField.isSaveEnabled = false
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(attributeSet)
        binding.etField.isSaveEnabled = false
    }

    private val errorState = "error"

    private fun init(attrs: AttributeSet?) {
        binding = ApEditTextViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        orientation = VERTICAL

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ApEditText)

        try {
            val errorText = ta.getString(R.styleable.ApEditText_errorText)
            binding.etErrorText.text = errorText

            val titleText = ta.getString(R.styleable.ApEditText_editTextTitle)
            binding.etFieldTitle.text = titleText

            val text = ta.getString(R.styleable.ApEditText_android_text)
            binding.etField.setText(text)

            val maxLength = ta.getInt(R.styleable.ApEditText_android_maxLength, 0)

            if (maxLength > 0) {
                binding.etField.filters += InputFilter.LengthFilter(maxLength)
            }

            val inputType = ta.getInt(R.styleable.ApEditText_android_inputType, 0)

            val hintText = ta.getString(R.styleable.ApEditText_hintText)
            binding.etField.hint = hintText

            if (inputType != 0) {
                binding.etField.inputType = inputType
            }
        } finally {
            ta.recycle()
        }

        binding.etField.addTextChangedListener(CreditCardTextWatcher())
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return MyState(superState, binding.etField.text.toString())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val myState = state as? MyState
        super.onRestoreInstanceState(myState?.superSaveState)

        binding.etField.setText(myState?.text ?: "")
    }

    fun listenTextChange(
        regexFormat: RegexFormat,
        onTextChange: (text: String, satisfyRegex: Boolean) -> Unit,
    ) {
        if (regexFormat == RegexFormat.ADDRESS_STREET) {
            binding.etField.filters += InputFilter.LengthFilter(40)
        } else if (regexFormat == RegexFormat.REGISTER_NAME_SURNAME) {
            binding.etField.filters += InputFilter.LengthFilter(35)
        }
        binding.etField.doAfterTextChanged {
            it?.let { text ->
                val isEnabled = checkRegex(text.toString(), regexFormat)
                if (isEnabled && binding.etField.tag == errorState) {
                    inactivateErrorState()
                }
                onTextChange(text.toString(), isEnabled)
            }
        }
    }

    fun updateText(text: String) {
        if (binding.etField.tag == errorState) {
            inactivateErrorState()
        }
        binding.etField.setText(text.trim())
    }

    fun getText(): String = binding.etField.text.toString()

    fun setErrorState() {
        binding.lnError.visibility = View.VISIBLE
        binding.etField.tag = errorState
        binding.etField.setBackgroundResource(R.drawable.et_error_background)
        binding.etErrorText.visibility = View.VISIBLE
    }

    fun setErrorState(errorMessage: String) {
        binding.lnError.visibility = View.VISIBLE
        binding.etField.tag = errorState
        binding.etField.setBackgroundResource(R.drawable.et_error_background)
        binding.etErrorText.text = errorMessage
        binding.etErrorText.visibility = View.VISIBLE
    }

    fun setDisable() {
        binding.etField.inputType = EditorInfo.TYPE_NULL
        binding.etField.isFocusable = false
        binding.etField.isClickable = false
        binding.etField.isLongClickable = false
    }

    fun inactivateErrorState() {
        binding.etField.tag = ""
        binding.etField.setBackgroundResource(R.drawable.rounded_rectangle_card_view_background)
        binding.etErrorText.visibility = View.GONE
        binding.lnError.visibility = View.GONE
    }

    private fun shouldActivateErrorState(editText: EditText, isFilled: Boolean): Boolean = !isFilled && !editText.isErrorStateActive

    fun listenFocusChangeAndUpdateErrorState(
        regexFormat: RegexFormat,
        vararg editTextsBeforeThisOne: ApEditText,
        requiredFieldList: BooleanArray = booleanArrayOf(),
        positions: IntArray = intArrayOf(),
    ) {
        binding.etField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!checkRegex(binding.etField.text.toString(), regexFormat)) {
                    setErrorState()
                } else {
                    inactivateErrorState()
                }
            } else {
                for (etPositions in editTextsBeforeThisOne.indices) {
                    val editText = editTextsBeforeThisOne[etPositions].binding.etField
                    if (
                        shouldActivateErrorState(editText, requiredFieldList[positions[etPositions]])
                    ) {
                        editTextsBeforeThisOne[etPositions].setErrorState()
                    }
                }
            }
        }
    }

    private fun checkRegex(text: String?, regexFormat: RegexFormat): Boolean = text?.let {
        when (regexFormat) {
            RegexFormat.REGISTER_NAME_SURNAME -> checkNameSurname(it)
            RegexFormat.REGISTER_EMAIL -> checkEmail(it)
            RegexFormat.ADDRESS_FIELD -> checkAddressField(it)
            RegexFormat.ADDRESS_STREET -> checkAddressText(it)
            RegexFormat.CARD_NAME_FIELD -> checkCardNameField(it)
            RegexFormat.INVITATION_CODE -> checkInvitationText(it)
            RegexFormat.CREDIT_CARD -> checkCreditCard(it)
        }
    } ?: false

    private fun checkNameSurname(textValue: String): Boolean = textValue.length >= 2 && textValue.trim() != ""

    private fun checkAddressField(textValue: String): Boolean = textValue.trim().isNotEmpty()

    private fun checkCardNameField(textValue: String): Boolean = textValue.isNotEmpty()

    private fun checkEmail(textValue: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(textValue).matches()

    private fun checkAddressText(textValue: String): Boolean = textValue.trim().isNotEmpty() && textValue.length <= 40

    private fun checkInvitationText(textValue: String): Boolean = textValue.trim().isNotEmpty() && textValue.length == 6

    private fun checkCreditCard(textValue: String): Boolean = textValue.replace(" ", "").length == 16

    fun updateFieldText(text: String) {
        binding.etField.setText(text)
    }

    enum class RegexFormat {
        REGISTER_NAME_SURNAME,
        REGISTER_EMAIL,
        ADDRESS_FIELD,
        ADDRESS_STREET,
        CARD_NAME_FIELD,
        INVITATION_CODE,
        CREDIT_CARD
    }

    @Parcelize
    class MyState(val superSaveState: Parcelable?, val text: String) :
        View.BaseSavedState(superSaveState), Parcelable

    private inner class CreditCardTextWatcher : TextWatcher {
        private var isUpdating: Boolean = false

        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            // No implementation needed
        }

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) return

            isUpdating = true
            val originalString = charSequence.toString().replace(" ", "")
            val formattedString = StringBuilder()
            for (i in originalString.indices) {
                if (i > 0 && i % 4 == 0) {
                    formattedString.append(" ")
                }
                formattedString.append(originalString[i])
            }
            binding.etField.setText(formattedString.toString())
            binding.etField.setSelection(formattedString.length)
            isUpdating = false
        }

        override fun afterTextChanged(editable: Editable?) {
            // No implementation needed
        }
    }
}
