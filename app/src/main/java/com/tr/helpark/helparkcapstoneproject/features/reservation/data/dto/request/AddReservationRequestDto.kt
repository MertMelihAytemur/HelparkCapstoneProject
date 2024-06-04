package com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class AddReservationRequestDto(
    val userId: Int,
    val carPlateId : Int,
    val parkId : Int,
    val resTime : Int,
    val hire : Float
) : UseCaseParams
