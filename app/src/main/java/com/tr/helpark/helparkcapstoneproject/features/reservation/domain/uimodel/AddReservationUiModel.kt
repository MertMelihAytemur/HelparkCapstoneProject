package com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import java.util.UUID

data class AddReservationUiModel(
    val resId : Int,
    val uniqueId : String = UUID.randomUUID().toString()
) : UiModel

sealed interface AddReservationApiState {
    object Initial : AddReservationApiState
    data class Success(val uiModel : AddReservationUiModel?) : AddReservationApiState
    data class Error(val error : UiError<ApiErrorModel>) : AddReservationApiState
}