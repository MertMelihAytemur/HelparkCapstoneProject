package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.dto.request.GetReservationHistoryRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.GetReservationHistoryUiModel
import tr.com.helpark.core.domain.UiResult

interface ReservationHistoryRepository {

    suspend fun getReservationHistory(
        getReservationHistoryRequestDto: GetReservationHistoryRequestDto
    ) : UiResult<GetReservationHistoryUiModel,ApiErrorModel>

}