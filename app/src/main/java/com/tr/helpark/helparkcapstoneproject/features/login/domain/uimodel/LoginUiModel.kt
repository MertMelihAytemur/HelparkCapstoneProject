package com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import java.util.UUID

data class LoginUiModel(
    val message: String?,
    val uniqueId : String = UUID.randomUUID().toString()
) : UiModel

sealed interface LoginApiState {
    object Initial : LoginApiState
    data class Success(val uiModel: LoginUiModel?) : LoginApiState
    data class Error(val error: UiError<ApiErrorModel>) : LoginApiState
}