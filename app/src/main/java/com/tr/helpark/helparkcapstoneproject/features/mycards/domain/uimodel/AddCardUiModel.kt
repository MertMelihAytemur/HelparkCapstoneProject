package com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import java.util.UUID

data class AddCardUiModel(
    val message : String? = null,
    val uniqueId : String = UUID.randomUUID().toString()
) : UiModel


sealed interface AddCardApiState {
    object Initial : AddCardApiState
    data class Success(val uiModel: AddCardUiModel?) : AddCardApiState
    data class Error(val error : UiError<ApiErrorModel>) : AddCardApiState
}