package com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel

data class SendOtpUiModel(
    val message : String?
) : UiModel

sealed interface SendOtpApiState {
    object Initial : SendOtpApiState
    data class Success(val uiModel: SendOtpUiModel?) : SendOtpApiState
    data class Error(val error: UiError<ApiErrorModel>) : SendOtpApiState
}