package com.tr.helpark.helparkcapstoneproject.features.register.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel

data class RegisterUiModel(
    val message : String?
) : UiModel


sealed interface RegisterApiState{
    object Initial : RegisterApiState
    data class Success(val uiModel: RegisterUiModel?) : RegisterApiState
    data class Error(val error: UiError<ApiErrorModel>) : RegisterApiState

}