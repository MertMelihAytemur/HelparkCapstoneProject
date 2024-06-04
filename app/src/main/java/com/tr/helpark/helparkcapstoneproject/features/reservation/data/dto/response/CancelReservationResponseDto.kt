package com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.CancelReservationUiModel

data class CancelReservationResponseDto(
    val message : String
)

fun CancelReservationResponseDto.toDomain() : CancelReservationUiModel {
    return CancelReservationUiModel(
        message = this.message
    )
}