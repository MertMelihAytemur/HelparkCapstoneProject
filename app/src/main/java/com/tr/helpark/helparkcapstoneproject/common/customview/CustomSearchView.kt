package com.tr.helpark.helparkcapstoneproject.common.customview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.CustomSearchViewBinding


class CustomSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CustomSearchViewBinding.inflate(LayoutInflater.from(context), this, true)

    private var onSearchActionListener: ((text: String) -> Unit)? = null
    private var onCloseActionListener: (() -> Unit?)? = null

    private val searchHandler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        onSearchActionListener?.invoke(binding.etSearch.text.toString())
    }

    fun setSearchActionListener(listener: (text: String) -> Unit) {
        this.onSearchActionListener = listener
    }

    fun setCloseButtonActionListener(listener: () -> Unit) {
        this.onCloseActionListener = listener
    }

    fun setQuerySearchView(query: String) {
        binding.etSearch.setText(query)
    }

    fun setHintSearchView(hint: String) {
        binding.etSearch.hint = hint
    }

    fun setHintTextColor(color: Int) {
        binding.etSearch.setHintTextColor(ContextCompat.getColor(context, color))
    }

    fun setSearchIcon(iconResId: Int) {
        binding.ivSearch.setImageResource(iconResId)
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSearchView)
        val searchHint = typedArray.getString(R.styleable.CustomSearchView_searchHint)
        typedArray.recycle()

        binding.etSearch.hint = searchHint ?: "no search view hint in xml !"

        binding.ivSearch.visibility = VISIBLE
        binding.ivClose.visibility = GONE


        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            binding.ivSearch.isVisible = !hasFocus
            binding.ivClose.isVisible = hasFocus
        }


        binding.ivClose.setOnClickListener {
            hideKeyboard()
            binding.etSearch.clearFocus()
            binding.etSearch.text.clear()
            onCloseActionListener?.invoke()
        }

        binding.ivSearch.setOnClickListener {
            onSearchActionListener?.invoke(binding.etSearch.text.toString())
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearchActionListener?.invoke(binding.etSearch.text.toString())
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchHandler.removeCallbacks(searchRunnable)
                searchHandler.postDelayed(searchRunnable, 600)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        binding.etSearch.clearFocus()
    }

    override fun onDetachedFromWindow() {
        searchHandler.removeCallbacks(searchRunnable)
        super.onDetachedFromWindow()
    }
}