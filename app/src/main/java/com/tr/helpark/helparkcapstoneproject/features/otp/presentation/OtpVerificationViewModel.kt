package com.tr.helpark.helparkcapstoneproject.features.otp.presentation

import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.SendOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.VerifyOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.SendOtpApiState
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.VerifyOtpApiState
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.usecase.SendOtpUseCase
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.usecase.VerifyOtpUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
@HiltViewModel
class OtpVerificationViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase
) : CoreViewModel(){

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    fun sendOtp(sendOtpUseCase: SendOtpRequestDto){
        launchRequest(
            requestBody = {
                sendOtpUseCase(sendOtpUseCase)
            },
            onSuccess = {uiModel ->
                val apiState = SendOtpApiState.Success(uiModel)
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.SEND_OTP_RESPONSE_RECEIVED,
                    sendOtpApiState = apiState
                )
            },
            onError = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.SEND_OTP_RESPONSE_RECEIVED,
                    sendOtpApiState = SendOtpApiState.Error(it)
                )
            }
        )
    }

    fun verifyOtp(verifyOtpRequestDto: VerifyOtpRequestDto){
        launchRequest(
            requestBody = {
                verifyOtpUseCase(verifyOtpRequestDto)
            },
            onSuccess = {uiModel ->
                val apiState = VerifyOtpApiState.Success(uiModel)
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.VERIFY_OTP_RESPONSE_RECEIVED,
                    verifyOtpApiState = apiState
                )
            },
            onError = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.VERIFY_OTP_RESPONSE_RECEIVED,
                    verifyOtpApiState = VerifyOtpApiState.Error(it)
                )
            }
        )
    }
    enum class PageEvent{
        INITIAL,
        SEND_OTP_RESPONSE_RECEIVED,
        VERIFY_OTP_RESPONSE_RECEIVED
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val sendOtpApiState: SendOtpApiState = SendOtpApiState.Initial,
        val verifyOtpApiState : VerifyOtpApiState = VerifyOtpApiState.Initial
    )
}