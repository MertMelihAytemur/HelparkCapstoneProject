package com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import java.util.UUID

data class RemoveCarUiModel(
    val message : String? = null,
    val uniqueId : String = UUID.randomUUID().toString()
) : UiModel


sealed interface RemoveCarApiState {
    object Initial : RemoveCarApiState
    data class Success(val data : RemoveCarUiModel?) : RemoveCarApiState
    data class Error(val error : UiError<ApiErrorModel>) : RemoveCarApiState
}