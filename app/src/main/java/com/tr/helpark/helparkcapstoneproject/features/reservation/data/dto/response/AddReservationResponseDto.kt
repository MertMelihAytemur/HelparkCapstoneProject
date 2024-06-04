package com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.AddReservationUiModel

data class AddReservationResponseDto(
    val message : Int
)

fun AddReservationResponseDto.toDomain() : AddReservationUiModel {
    return AddReservationUiModel(
        resId = message
    )
}