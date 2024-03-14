package com.tr.helpark.helparkcapstoneproject.features.otp.presentation


import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.customview.MessageType
import com.tr.helpark.helparkcapstoneproject.common.extensions.boldNumbersAndAsterisks
import com.tr.helpark.helparkcapstoneproject.common.extensions.formatAndInsertPhoneNumber
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentOtpVerificationBinding


class OtpVerificationFragment :
    BaseFragment<FragmentOtpVerificationBinding, OtpVerificationViewModel>(
        FragmentOtpVerificationBinding::inflate,
    ) {

    override val viewModel: OtpVerificationViewModel by viewModels()
    private val args by navArgs<OtpVerificationFragmentArgs>()

    private var gsmNo: String? = null
    private var otpMessage: String? = null

    override fun onViewReady() {
        super.onViewReady()
        setArguments()
    }

    override fun initListeners() {
        binding.otpCodeView.isVerifyCodeLengthListener { isLength ->
            binding.btnVerify.isEnabled = isLength
        }

        binding.btnVerify.setOnClickListener {
            showTopAlertMessage("Doğrulama Kodu Hatalı. Tekrar Deneyiniz.")
            val otpCode = binding.otpCodeView.getOtpCode()
            if (otpCode.length == 6) {
                //gsmNo?.let { viewModel.getTokenWithOtpCode(gsm=it, otp = otpCode, nomuToken = nomuToken!!, partnerID = partnerId) }
            }
        }
        binding.otpTimerView.againTextClickListener {
            //gsmNo?.let { viewModel.resendOtpCodeWithGsmUseCase(it,partnerId) }
        }

        binding.btnBackOtp.setOnClickListener {
            findNavController().popBackStack(R.id.loginFragment,false)
        }
    }

    private fun setArguments() {
        gsmNo = args.gsmNo
        setTimerView(60)
        setOtpMessage()
    }

    private fun setOtpMessage() {
        gsmNo?.let {
            otpMessage = getString(
                R.string.please_enter_the_verification_code_sent_to_your_number
            ).formatAndInsertPhoneNumber(it)
        }

        otpMessage?.let {
            binding.txtOtpPhoneMessage.text = it.boldNumbersAndAsterisks()
        }
    }

    private fun showTopAlertMessage(
        message: String,
        clickCommentAction: (() -> Any)? = null,
    ) {
        binding.btnVerify.isEnabled = false
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