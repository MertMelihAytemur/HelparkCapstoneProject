package com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import javax.inject.Inject

class ToggleFavoriteUiModel @Inject constructor(
    val isFavorite: Boolean
) : UiModel

sealed interface ToggleFavoriteApiState {
    object Initial : ToggleFavoriteApiState
    data class Success(val uiModel: ToggleFavoriteUiModel?) : ToggleFavoriteApiState
    data class Error(val error: UiError<ApiErrorModel>) : ToggleFavoriteApiState
}