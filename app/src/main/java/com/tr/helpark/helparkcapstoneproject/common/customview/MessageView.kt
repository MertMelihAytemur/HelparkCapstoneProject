package com.tr.helpark.helparkcapstoneproject.common.customview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.gone
import com.tr.helpark.helparkcapstoneproject.databinding.MessageViewAlertBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class MessageType {
    ALERT,
    CONFIRMATION
}

class MessageView @JvmOverloads constructor(
    context: Context,
    private val attributeSet: AttributeSet,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr){
    private val binding: MessageViewAlertBinding
    private var hasSingleShown = false
    var messageType: MessageType? = null
    var messageText: String? = ""

    init {
        binding =
            MessageViewAlertBinding.inflate(LayoutInflater.from(context), this, false)
        addView(binding.root)
        binding.root.gone()
        build()
        initListeners()
    }

    private fun getXmlAttributes() {
        context.obtainStyledAttributes(attributeSet, R.styleable.MessageView).apply {
            messageType = MessageType.values()
                .getOrNull(getInt(R.styleable.MessageView_messageType, -1))
            messageText = getString(R.styleable.MessageView_message)
            recycle()
        }
    }

    private fun makeAlertInit() {
        binding.apply {
            clMessageView.setBackgroundColor(ContextCompat.getColor(context,R.color.light_red))
            iwWarning.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_warning))
            btnCloseAlert.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_delete_disabled))
            messageText?.let { setTextWithBold(it) }
        }
    }

    private fun makeConfirmInit() {
        binding.apply {
            clMessageView.setBackgroundColor(ContextCompat.getColor(context,R.color.light_green))
            iwWarning.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_task_alt_confirm))
            btnCloseAlert.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_delete__disabled))
            messageText?.let { setTextWithBold(it) }
        }
    }

    fun build() {
        when (messageType) {
            MessageType.ALERT -> makeAlertInit()
            MessageType.CONFIRMATION -> makeConfirmInit()
            null -> {
                getXmlAttributes()
            }
        }
    }



    private fun setTextWithBold(text: CharSequence) {
        val spannableStringBuilder = SpannableStringBuilder(text)
        val startTag = "/b/"
        val endTag = "/.b/"
        var startIndex = text.indexOf(startTag)
        while (startIndex >= 0) {
            val endIndex = text.indexOf(endTag, startIndex)
            if (endIndex > startIndex) {
                spannableStringBuilder.setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableStringBuilder.delete(endIndex, endIndex + endTag.length)
                spannableStringBuilder.delete(startIndex, startIndex + startTag.length)
                startIndex -= startTag.length
            }
            startIndex = text.indexOf(startTag, endIndex)
        }
        binding.txtCommentAlert.text = spannableStringBuilder
    }


    private fun initListeners() {
        binding.btnCloseAlert.setOnClickListener {
            hide()
            closeButtonListener?.invoke()
        }
        binding.txtCommentAlert.setOnClickListener {
            commentClickListener?.invoke()
        }
    }


    private var closeButtonListener: (() -> Unit)? = null

    fun setCloseButtonListener(listener: () -> Unit) {
        closeButtonListener = listener
    }

    private var commentClickListener: (() -> Unit)? = null

    fun setCommentClickListener(listener: () -> Unit) {
        commentClickListener = listener
    }

    fun show(timeMillis:Long?=null) {
        if (timeMillis != null) {
            binding.btnCloseAlert.visibility = View.GONE
            CoroutineScope(Dispatchers.Main).launch {
                delay(timeMillis)
                hide()
            }
        }
        binding.root.translationX = -binding.root.width.toFloat()
        binding.root.visibility = View.VISIBLE
        val animation = AnimatorSet().apply {
            val slideIn = ObjectAnimator.ofFloat(binding.root, View.TRANSLATION_X, -binding.root.width.toFloat(), 0f)
            duration = 500
            play(slideIn)
        }
        animation.start()
    }

    fun hide() {
        val animation = AnimatorSet().apply {
            val slideOut = ObjectAnimator.ofFloat(binding.root, View.TRANSLATION_X, 0f, -binding.root.width.toFloat())
            duration = 500
            play(slideOut)
        }
        animation.start()
        animation.doOnEnd {
            binding.root.visibility = View.GONE
            binding.root.translationX = 0f
        }
        hasSingleShown = false
    }

    fun showOnlyOnce(timeMillis:Long?=null) {
        if (hasSingleShown) return

        if (timeMillis != null) {
            binding.btnCloseAlert.visibility = View.GONE
            CoroutineScope(Dispatchers.Main).launch {
                delay(timeMillis)
                hide()
            }
        }
        binding.root.translationX = -binding.root.width.toFloat()
        binding.root.visibility = View.VISIBLE
        val animation = AnimatorSet().apply {
            val slideIn = ObjectAnimator.ofFloat(binding.root, View.TRANSLATION_X, -binding.root.width.toFloat(), 0f)
            duration = 500
            play(slideIn)
        }
        animation.start()

        hasSingleShown = true
    }


}