package com.tr.helpark.helparkcapstoneproject.features.otp.presentation


import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tr.helpark.helparkcapstoneproject.common.customview.MessageType
import com.tr.helpark.helparkcapstoneproject.common.extensions.boldNumbersAndAsterisks
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentOtpVerificationBinding


class OtpVerificationFragment :
    BaseFragment<FragmentOtpVerificationBinding, OtpVerificationViewModel>(
        FragmentOtpVerificationBinding::inflate,
    ) {

    override val viewModel: OtpVerificationViewModel by viewModels()


    override fun initListeners() {
        binding.otpCodeView.isVerifyCodeLengthListener { isLength ->
            binding.btnVerify.isEnabled = isLength
        }
        binding.btnVerify.setOnClickListener {
            val otpCode = binding.otpCodeView.getOtpCode()
            if (otpCode.length == 6) {
                //gsmNo?.let { viewModel.getTokenWithOtpCode(gsm=it, otp = otpCode, nomuToken = nomuToken!!, partnerID = partnerId) }
            }
        }
        binding.otpTimerView.againTextClickListener {
            //gsmNo?.let { viewModel.resendOtpCodeWithGsmUseCase(it,partnerId) }
        }
        binding.btnBackOtp.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun otpMessage(message: String) {
        binding.txtOtpPhoneMessage.text = message.boldNumbersAndAsterisks()
    }

    private fun showTopAlertMessage(
        message: String,
        clickCommentAction: (() -> Any)? = null,
    ) {
        binding.customErrorMessageView.apply {
            show()
            this.messageText = message
            this.messageType = MessageType.ALERT
            this.setCommentClickListener { clickCommentAction?.invoke() }
            build()
        }
    }

    private fun setTimerView(otpTimeSec: Int?) {
        otpTimeSec?.let {
            binding.otpTimerView.startOtpTimer(it)
        }
    }
}