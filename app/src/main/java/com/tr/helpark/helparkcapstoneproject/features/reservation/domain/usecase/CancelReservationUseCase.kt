package com.tr.helpark.helparkcapstoneproject.features.reservation.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.CancelReservationRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.ReservationRepository
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.CancelReservationUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class CancelReservationUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) : UseCase<CancelReservationUiModel, ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<CancelReservationUiModel, ApiErrorModel> {
        return reservationRepository.cancelReservation(params as CancelReservationRequestDto)
    }
}