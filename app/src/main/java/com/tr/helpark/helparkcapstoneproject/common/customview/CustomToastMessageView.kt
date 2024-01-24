package com.tr.helpark.helparkcapstoneproject.common.customview

import android.os.Looper
import android.view.View
import android.view.LayoutInflater
import com.tr.helpark.helparkcapstoneproject.R
import android.util.AttributeSet
import android.content.Context
import android.os.Handler
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tr.helpark.helparkcapstoneproject.common.extensions.gone
import com.tr.helpark.helparkcapstoneproject.databinding.CustomToastMessageViewBinding


/**
 *Created by Mert Melih Aytemur on 1/19/2024.
 */
class CustomToastMessageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomToastMessageViewBinding

    init {
        binding =
            CustomToastMessageViewBinding.inflate(LayoutInflater.from(context), this, true)
        binding.root.gone()
    }

    fun setToastMessageText(toastText: String) {
        binding.tvToastMessage.text = toastText
    }

    fun setToastStatus(isPositive: Boolean) {
        if (isPositive) {
            binding.rootCustomToastView.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.green_primary_light)
            binding.ivToastStatus.setImageResource(R.drawable.ic_toast_message_success)
        } else {
            binding.rootCustomToastView.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.orange_primary_light)
            binding.ivToastStatus.setImageResource(R.drawable.ic_toast_message_failure)
        }
    }

    fun show(timeMillis: Long? = null) {
        binding.root.visibility = View.VISIBLE
        timeMillis?.let {
            Handler(Looper.getMainLooper()).postDelayed({
                hide()
            }, timeMillis)
        }
        // Alpha animation
        binding.root.alpha = 0f
        binding.root.animate()
            .alpha(1f)
            .setDuration(500)
            .start()
    }

    private fun hide() {
        binding.root.animate()
            .alpha(0f)
            .setDuration(500)
            .withEndAction {
                binding.root.visibility = View.GONE
                binding.root.alpha = 1f
            }
            .start()
    }
}