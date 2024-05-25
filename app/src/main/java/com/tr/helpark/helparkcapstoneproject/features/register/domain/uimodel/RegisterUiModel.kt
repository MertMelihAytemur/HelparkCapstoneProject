package com.tr.helpark.helparkcapstoneproject.features.register.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import java.util.UUID

data class RegisterUiModel(
    val message : String?,
    val uniqueId : String = UUID.randomUUID().toString()
) : UiModel


sealed interface RegisterApiState{
    object Initial : RegisterApiState
    data class Success(val uiModel: RegisterUiModel?) : RegisterApiState
    data class Error(val error: UiError<ApiErrorModel>) : RegisterApiState

}