package com.tr.helpark.helparkcapstoneproject.features.reservation.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.AddReservationRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.AddReservationUiModel
import tr.com.helpark.core.domain.UiResult

interface ReservationRepository {
    suspend fun addReservation(
        addReservationRequestDto: AddReservationRequestDto
    ) : UiResult<AddReservationUiModel, ApiErrorModel>
}