package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.util.calculateDensity
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem

fun TextView.addClickableLink(
    fullText: String,
    linkTextFirst: SpannableString,
    colorRes: Int? = null,
    callBackTextFirst: () -> Unit,
) {
    // Create a ClickableSpan for the first link text
    val clickableSpanFirst = object : ClickableSpan() {
        override fun onClick(widget: View) {
            callBackTextFirst.invoke()
            widget.invalidate() // Refresh the widget to apply visual changes
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            colorRes?.let { color ->
                ds.color = ResourcesCompat.getColor(resources, color, null) // Apply color
            }
            ds.isUnderlineText = true // Show links with underlines
        }
    }

    // Apply the ClickableSpan to the link text
    linkTextFirst.setSpan(
        clickableSpanFirst,
        0,
        linkTextFirst.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Apply a StyleSpan for bold text to the link text
    linkTextFirst.setSpan(
        StyleSpan(Typeface.BOLD),
        0,
        linkTextFirst.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Prepare the full text by replacing the placeholder with the first link text
    val fullTextWithTemplate = fullText.replace(linkTextFirst.toString(), "^1", false)

    // Expand the template and set the resulting text to the TextView
    val cs = TextUtils.expandTemplate(fullTextWithTemplate, linkTextFirst)
    text = cs

    // Make the link clickable
    movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.setParkIsOpenStatus(isOpen: Boolean) {
    with(this) {
        if (isOpen) {
            text = this.context.getString(R.string.park_open)
            setTextColor(ContextCompat.getColor(context, R.color.purple_500))
        } else {
            text = context.getString(R.string.park_closed)
            setTextColor(ContextCompat.getColor(context, R.color.coral_red))
        }
    }
}

fun TextView.setTextViewAlphaAnimation() {
    val liveAnimation = AlphaAnimation(0.1f, 1.0f)
    liveAnimation.duration = 600
    liveAnimation.repeatCount = Animation.INFINITE
    liveAnimation.repeatMode = Animation.REVERSE
    this.startAnimation(liveAnimation)
}

fun TextView.setParkDensityStatus(park: GetAllParksUiModelItem) {
    with(this) {
        park.let {
            val density = calculateDensity(
                ((park.capacity ?: 0) - (park.emptyCapacity ?: 0)).toDouble(),
                park.capacity?.toDouble() ?: 0.0
            )

            text = "${(park.capacity ?: 0) - (park.emptyCapacity ?: 0)} / ${park.capacity}"

            density.let {
                when (it) {
                    in 0.0..33.3 -> {
                        setBackgroundResource(R.drawable.bg_button_selector_green)
                    }

                    in 33.3..66.6 -> {
                        setBackgroundResource(R.drawable.bg_button_selector_orange)
                    }

                    in 66.6..100.0 -> {
                        setBackgroundResource(R.drawable.bg_button_selector_red)
                    }
                }
            }
        }
    }
}