package com.tr.helpark.helparkcapstoneproject.common.customview

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.OtpTimerViewBinding

class OTPTimerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding = OtpTimerViewBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
        initView()
    }

    private fun initView() {
        binding.tvOtpAgain.isOtpTextEnabledColor(isEnabled = false)
    }


    fun againTextClickListener(againTextClick: (view: View) -> Unit) {
        binding.tvOtpAgain.setOnClickListener {
            againTextClick.invoke(it)
            binding.tvOtpAgain.isOtpTextEnabledColor(isEnabled = false)
            binding.tvOtpTimer.visibility = View.VISIBLE
        }
    }

    fun startOtpTimer(seconds: Int) {
        val countdownTimer = object : CountDownTimer((seconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingSeconds = (millisUntilFinished / 1000).toInt()
                setTimerProgress(seconds, remainingSeconds)
                setOtpTimerText(remainingSeconds)
            }

            override fun onFinish() {
                binding.tvOtpAgain.isOtpTextEnabledColor(isEnabled = true)
            }
        }
        countdownTimer.start()
    }

    private fun setTimerProgress(totalSeconds: Int, elapsedTime: Int) {
        val progress = ((elapsedTime.toDouble() / totalSeconds.toDouble()) * 100).toInt()
        binding.pgOtp.progress = progress
    }


    fun setOtpTimerText(time: Int) {
        if (time <= 0) {
            binding.tvOtpTimer.visibility = View.INVISIBLE
            binding.tvOtpAgain.isOtpTextEnabledColor(isEnabled = true)
        } else {
            val timerText = resources.getString(R.string.otp_second_format, time)
            binding.tvOtpTimer.text = timerText
        }
    }

    private fun TextView.isOtpTextEnabledColor(isEnabled: Boolean) {
        this.isEnabled = isEnabled
        val textColorResId = if (isEnabled) {
            R.color.blue
        } else {
            R.color.text_color_black
        }
        this.setTextColor(resources.getColor(textColorResId, null))
    }

}