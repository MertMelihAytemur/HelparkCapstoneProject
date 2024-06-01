package com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import java.util.UUID

data class AddCarUiModel(
    val message: String? = null,
    val uniqueId: String = UUID.randomUUID().toString()
) : UiModel


sealed interface AddCarApiState {
    object Initial : AddCarApiState
    data class Success(val data: AddCarUiModel?) : AddCarApiState
    data class Error(val error: UiError<ApiErrorModel>) : AddCarApiState
}