package com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel

data class DeleteUserAccountUiModel(
    val message : String? = null
) : UiModel


sealed interface DeleteUserAccountApiState {
    data class Success(val uiModel: DeleteUserAccountUiModel?) : DeleteUserAccountApiState
    data class Error(val error: UiError<ApiErrorModel>) : DeleteUserAccountApiState
    object Initial : DeleteUserAccountApiState
}