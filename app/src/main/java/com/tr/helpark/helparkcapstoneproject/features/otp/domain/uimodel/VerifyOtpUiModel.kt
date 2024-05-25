package com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import java.util.UUID

data class VerifyOtpUiModel(
    val message : String?,
    val uniqueId : String = UUID.randomUUID().toString()
) : UiModel

sealed interface VerifyOtpApiState {
    object Initial : VerifyOtpApiState
    data class Success(val uiModel: VerifyOtpUiModel?) : VerifyOtpApiState
    data class Error(val error: UiError<ApiErrorModel>) : VerifyOtpApiState
}