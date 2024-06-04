package com.tr.helpark.helparkcapstoneproject.features.reservation.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.AddReservationRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.ReservationRepository
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.AddReservationUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class AddReservationUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) : UseCase<AddReservationUiModel, ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<AddReservationUiModel, ApiErrorModel> {
        return reservationRepository.addReservation(params as AddReservationRequestDto)
    }
}