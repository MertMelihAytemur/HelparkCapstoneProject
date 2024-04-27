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
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

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
    linkTextFirst.setSpan(clickableSpanFirst, 0, linkTextFirst.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    // Apply a StyleSpan for bold text to the link text
    linkTextFirst.setSpan(StyleSpan(Typeface.BOLD), 0, linkTextFirst.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    // Prepare the full text by replacing the placeholder with the first link text
    val fullTextWithTemplate = fullText.replace(linkTextFirst.toString(), "^1", false)

    // Expand the template and set the resulting text to the TextView
    val cs = TextUtils.expandTemplate(fullTextWithTemplate, linkTextFirst)
    text = cs

    // Make the link clickable
    movementMethod = LinkMovementMethod.getInstance()
}