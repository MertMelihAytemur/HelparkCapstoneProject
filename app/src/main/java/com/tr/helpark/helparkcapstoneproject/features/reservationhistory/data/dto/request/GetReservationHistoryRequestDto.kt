package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class GetReservationHistoryRequestDto(
    val userId : Int
) : UseCaseParams
