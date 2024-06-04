package com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class CancelReservationRequestDto(
    val resId : Int
) : UseCaseParams
