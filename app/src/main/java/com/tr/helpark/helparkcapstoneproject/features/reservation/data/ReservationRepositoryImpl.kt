package com.tr.helpark.helparkcapstoneproject.features.reservation.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.AddReservationRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.remote.ReservationService
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.ReservationRepository
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.AddReservationUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val reservationService: ReservationService
) : ReservationRepository, ApiExecutor {
    override suspend fun addReservation(addReservationRequestDto: AddReservationRequestDto): UiResult<AddReservationUiModel, ApiErrorModel> {
        val apiResult = execute {
            reservationService.addReservation(addReservationRequestDto)
        }

        return when (apiResult) {
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }
}