package com.tr.helpark.helparkcapstoneproject.common.customview

import android.content.Context
import android.os.Parcelable
import android.text.InputFilter
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

/**
 * Custom EditText View handle various events related to editText
 */
@SuppressWarnings("TooManyFunctions")
class ApEditText : LinearLayout {

    lateinit var binding : ApEditTextViewLayoutBinding
    constructor(context: Context) : super(context) {
    }

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

    /**
     * initialize custom attributes
     * errorText is text to be shown on user entered text that not meet regex requirements
     * editTextTitle is title of editText
     * inputType is input type format for editText
     *
     * @see [R.styleable.ApEditText]
     */
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

    /**
     * Listen text change on editText and checks the text value whether matches regex or not
     * @param regexFormat regex format type for [etField] text
     * @param onTextChange invokes whenever user change text in [etField]
     * text is the entered text and satisfyRegex is boolean value
     * indicating whether [etField] text matches regex or not
     */
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

    /**
     * Updates [etField] text with
     * @param text value
     */
    fun updateText(text: String) {
        if (binding.etField.tag == errorState) {
            inactivateErrorState()
        }
        binding.etField.setText(text.trim())
    }

    /**
     * Returns [etField] text value as String
     */
    fun getText():
        String = binding.etField.text.toString()

    /**
     * change [etField] background drawable to error state and shows [etErrorText]
     */
    fun setErrorState() {
        binding.lnError.visibility = View.VISIBLE
        binding.etField.tag = errorState
        binding.etField.setBackgroundResource(R.drawable.et_error_background)
        binding.etErrorText.visibility = View.VISIBLE
    }

    /**
     * change [etField] background drawable to error state and shows [etErrorText]
     */
    fun setErrorState(errorMessage: String) {
        binding.lnError.visibility = View.VISIBLE
        binding.etField.tag = errorState
        binding.etField.setBackgroundResource(R.drawable.et_error_background)
        binding.etErrorText.text = "hata"
        binding.etErrorText.visibility = View.VISIBLE
    }

    /**
     * change [etField] not editable
     */
    fun setDisable() {
        binding.etField.inputType = EditorInfo.TYPE_NULL
        binding.etField.isFocusable = false
        binding.etField.isClickable = false
        binding.etField.isLongClickable = false
    }

    /**
     * change [etField] background to normal state
     */
    fun inactivateErrorState() {
        binding.etField.tag = ""
        binding.etField.setBackgroundResource(R.drawable.rounded_rectangle_card_view_background)
        binding.etErrorText.visibility = View.GONE
        binding.lnError.visibility = View.GONE
    }

    /**
     * decides whether the given [editText] field should show an error or not
     * @param isFilled is value that contains information of
     * [editText] text matches given regex or not. if [isFilled] true than text matches regex.
     */
    private fun shouldActivateErrorState(editText: EditText, isFilled: Boolean):
        Boolean = !isFilled && !editText.isErrorStateActive

    /**
     * edit textdeki focus değişikliğini takip ediyor bu method
     * eğer bu edit text focusu kaybettiyse editTextin icindeki text [regexFormat] daki regex
     * ile uyusuyor mu ona bakıyor uyuşmuyorsa hata mesaji gösteriyor.
     *
     *@param editTextsBeforeThisOne bu alan ekranda bu editTextden daha önce gösterilen editTextlerin
     *  listesinin döndüğü alan bunun amacıda ekranda bu editTextden önce gösterilen editTextler
     *  boş bırakılmış veya regex ile uyuşmuyorsa önce o alanı doldurun maksadıyla o
     *  editTextlerde hata mesajı göstermek
     *
     * @param requiredFieldList ekrandaki editTextlerin ekrandaki sırasıyla
     * regexe uygun girilip girilmediğini takip ettiğimiz boolean array
     *
     * @param positions girilen [editTextsBeforeThisOne] daki editTextlerin [requiredFieldList]
     * icinde hangi pozisyonda olduğunu tutan int arrayi
     *
     * Örnek method
     * listenFocusChangeAndUpdateErrorState(
     * ApEditText.RegexFormat.ADDRESS_FIELD,
     * viewBinding.etStreet,
     * requiredFieldList = viewModel.allFieldFilledList,  değeri (true,false,true,true)
     * positions = intArrayOf(0))
     *
     * positions daki değer requiredFieldList de 0ıncı
     * elemanın yani viewBinding.etStreet temsil ettiğini gösteriyor
     */
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
                /**
                 *----
                 * Bu kısım kullanıcı bu editTexe tıkladığı zaman ekranda sıralama olarak bu editText ten
                 * önce gösterilen diğer edit textlerin boş bırakıldığı yada regexe uymadığı durumda
                 * bu editTextten önceki alanlarında hata formatını göstermesi için kullanılıyor.
                 * Her editTextin ekrandaki sırasıyla doğru formatta girilmesi için  böyle bir yapı kullanıldı
                 * ----
                 */
                for (etPositions in editTextsBeforeThisOne.indices) {
                    val editText = editTextsBeforeThisOne[etPositions].binding.etField
                    if (
                        shouldActivateErrorState
                        (editText, requiredFieldList[positions[etPositions]])
                    ) {
                        editTextsBeforeThisOne[etPositions].setErrorState()
                    }
                }
            }
        }
    }

    /**
     * Checks if the regex matches the text
     */
    private fun checkRegex(text: String?, regexFormat: RegexFormat):
        Boolean = text?.let {
            when (regexFormat) {
                RegexFormat.REGISTER_NAME_SURNAME -> checkNameSurname(it)
                RegexFormat.REGISTER_EMAIL -> checkEmail(it)
                RegexFormat.ADDRESS_FIELD -> checkAddressField(it)
                RegexFormat.ADDRESS_STREET -> checkAddressText(it)
                RegexFormat.CARD_NAME_FIELD -> checkCardNameField(it)
                RegexFormat.INVITATION_CODE -> checkInvitationText(it)
            }
        } ?: false

    private fun checkNameSurname(textValue: String):
        Boolean = textValue.length >= 2 && textValue.trim() != ""

    private fun checkAddressField(textValue: String):
        Boolean = textValue.trim().isNotEmpty()

    private fun checkCardNameField(textValue: String):
        Boolean = textValue.isNotEmpty()

    private fun checkEmail(textValue: String):
        Boolean = Patterns.EMAIL_ADDRESS.matcher(textValue).matches()

    private fun checkAddressText(textValue: String):
        Boolean = textValue.trim().isNotEmpty() && textValue.length <= 40

    private fun checkInvitationText(textValue: String):
        Boolean = textValue.trim().isNotEmpty() && textValue.length == 6

    fun updateFieldText(text : String){
        binding.etField.setText(text)
    }

    /**
     * Regex format for different type of [EditText]
     */
    enum class RegexFormat {
        REGISTER_NAME_SURNAME,
        REGISTER_EMAIL,
        ADDRESS_FIELD,
        ADDRESS_STREET,
        CARD_NAME_FIELD,
        INVITATION_CODE
    }

    /**
     * Class for saving state of view
     */
    @Parcelize
    class MyState(val superSaveState: Parcelable?, val text: String) :
        View.BaseSavedState(superSaveState), Parcelable
}
