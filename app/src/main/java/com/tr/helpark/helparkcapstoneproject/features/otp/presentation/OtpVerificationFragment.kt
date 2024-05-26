package com.tr.helpark.helparkcapstoneproject.features.otp.presentation


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.helpark.helpark.common.utils.preferences.PreferencesKeys.KEY_USER_ID
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.customview.MessageType
import com.tr.helpark.helparkcapstoneproject.common.extensions.boldNumbersAndAsterisks
import com.tr.helpark.helparkcapstoneproject.common.extensions.formatAndInsertPhoneNumber
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentOtpVerificationBinding
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.SendOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.VerifyOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.SendOtpApiState
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.VerifyOtpApiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class OtpVerificationFragment :
    BaseFragment<OtpVerificationViewModel, FragmentOtpVerificationBinding>(
        FragmentOtpVerificationBinding::inflate,
    ) {

    override val viewModel: OtpVerificationViewModel by viewModels()

    private var gsmNo: String? = null
    private var otpMessage: String? = null

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setArguments()
        initListeners()

        collectPageState(viewModel.pageStateFlow) { state ->
            when (state.pageEvent) {
                OtpVerificationViewModel.PageEvent.INITIAL -> {

                }

                OtpVerificationViewModel.PageEvent.SEND_OTP_RESPONSE_RECEIVED -> {
                    onSendOtpResponseReceived(state.sendOtpApiState)
                }

                OtpVerificationViewModel.PageEvent.VERIFY_OTP_RESPONSE_RECEIVED -> {
                    onVerifyOtpResponseReceived(state.verifyOtpApiState)
                }
            }
        }
    }

    private fun onSendOtpResponseReceived(sendOtpApiState: SendOtpApiState) {
        when (sendOtpApiState) {
            is SendOtpApiState.Initial -> {

            }

            is SendOtpApiState.Success -> {
                showToastMessage("Doğrulama Kodu Gönderildi.", toastType = ToastMessageType.GENERAL_SUCCESS)
            }

            is SendOtpApiState.Error -> {
                handleNetworkError(sendOtpApiState.error)
            }
        }
    }

    private fun onVerifyOtpResponseReceived(verifyOtpApiState: VerifyOtpApiState) {
        when (verifyOtpApiState) {
            is VerifyOtpApiState.Initial -> {}

            is VerifyOtpApiState.Success -> {
                preferencesManager.putString(KEY_USER_ID, verifyOtpApiState.uiModel?.userId.orEmpty())

                showToastMessage("Doğrulama Başarılı.", toastType = ToastMessageType.GENERAL_SUCCESS)
                navigateWithAnimation(R.id.action_otpVerificationFragment_to_homeFragment)
            }

            is VerifyOtpApiState.Error -> {
                showTopAlertMessage("Doğrulama Kodu Hatalı.")
            }
        }
    }

    private fun initListeners() {
        binding.otpCodeView.isVerifyCodeLengthListener { isLength ->
            binding.btnVerify.isEnabled = isLength
        }

        binding.btnVerify.setOnClickListener {
            val otpCode = binding.otpCodeView.getOtpCode()
            if (otpCode.length == 6) {
                gsmNo?.let { phoneNumber ->
                    viewModel.verifyOtp(
                        VerifyOtpRequestDto(
                            phoneNumber = phoneNumber,
                            otp = otpCode
                        )
                    )
                }
            }
        }

        binding.otpTimerView.againTextClickListener {
            binding.otpTimerView.startOtpTimer(60)
        }

        binding.btnBackOtp.setOnClickListener {
            findNavController().popBackStack(R.id.loginFragment, false)
        }
    }

    private fun setArguments() {
        gsmNo = arguments?.getString(KEY_ARGUMENT_GSM_NO)
        sendOtpRequest()
        setTimerView(60)
        setOtpMessage()
    }

    private fun sendOtpRequest(){
        gsmNo?.let { phoneNumber ->
            viewModel.sendOtp(SendOtpRequestDto(phoneNumber))
        }
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


    companion object{
        const val KEY_ARGUMENT_GSM_NO = "gsmNo"
    }
}