package com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog

import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.AddReservationRequestDto

interface IAddReservationAction {

    fun addReservation(addReservationRequestDto: AddReservationRequestDto)
}