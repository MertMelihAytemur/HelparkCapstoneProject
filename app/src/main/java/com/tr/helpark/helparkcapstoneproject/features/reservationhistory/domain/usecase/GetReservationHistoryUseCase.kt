package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.dto.request.GetReservationHistoryRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.ReservationHistoryRepository
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.GetReservationHistoryUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class GetReservationHistoryUseCase @Inject constructor(
    private val reservationHistoryRepository: ReservationHistoryRepository
) : UseCase<GetReservationHistoryUiModel,ApiErrorModel>{
    override suspend fun invoke(params: UseCaseParams?): UiResult<GetReservationHistoryUiModel, ApiErrorModel> {
        return reservationHistoryRepository.getReservationHistory(params as GetReservationHistoryRequestDto)
    }
}