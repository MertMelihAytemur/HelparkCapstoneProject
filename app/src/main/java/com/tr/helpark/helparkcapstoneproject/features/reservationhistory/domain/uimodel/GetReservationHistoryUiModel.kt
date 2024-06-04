package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel

import tr.com.helpark.core.domain.UiModel

data class GetReservationHistoryUiModel(
    val reservations : List<ReservationHistoryItemUiModel>?
) : UiModel

data class ReservationHistoryItemUiModel(
    val carPlateId : Int? = null,
    val hire : Int? = null,
    val id : Int? = null,
    val parkId : Int? = null,
    val resDate : String? = null,
    val resTime : Int? = null,
    val status : Int? = null,
    val userId : Int? = null
)