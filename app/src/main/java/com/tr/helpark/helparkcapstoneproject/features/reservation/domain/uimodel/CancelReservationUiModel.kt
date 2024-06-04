package com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel

data class CancelReservationUiModel(
    val message : String
) : UiModel


sealed interface CancelReservationApiState {
    object Initial : CancelReservationApiState
    data class Success(val uiModel : CancelReservationUiModel?) : CancelReservationApiState
    data class Error(val error : UiError<ApiErrorModel>) : CancelReservationApiState
}