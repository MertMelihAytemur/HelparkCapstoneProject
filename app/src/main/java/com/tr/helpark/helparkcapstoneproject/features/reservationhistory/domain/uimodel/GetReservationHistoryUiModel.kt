package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel

data class GetReservationHistoryUiModel(
    val reservations : List<ReservationHistoryItemUiModel>?
) : UiModel

data class ReservationHistoryItemUiModel(
    val carPlateId : Int? = null,
    val hire : Float? = null,
    val id : Int? = null,
    val parkId : Int? = null,
    val resDate : String? = null,
    val resTime : Int? = null,
    val status : Int? = null,
    val userId : Int? = null,
    val parkName : String? = null,
    val latitude : String? = null,
    val longitude : String? = null,
)

sealed interface GetReservationHistoryApiState {
    object Initial : GetReservationHistoryApiState
    data class Success(val uiModel : GetReservationHistoryUiModel?) : GetReservationHistoryApiState
    data class Error(val error : UiError<ApiErrorModel>) : GetReservationHistoryApiState
}