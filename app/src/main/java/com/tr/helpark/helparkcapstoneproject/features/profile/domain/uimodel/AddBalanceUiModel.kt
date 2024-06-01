package com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel


data class AddBalanceUiModel(
    val message : String? = null
) : UiModel


sealed interface AddBalanceApiState {
    object Initial : AddBalanceApiState
    data class Success(val uiModel : AddBalanceUiModel?) : AddBalanceApiState
    data class Error(val error : UiError<ApiErrorModel>) : AddBalanceApiState
}