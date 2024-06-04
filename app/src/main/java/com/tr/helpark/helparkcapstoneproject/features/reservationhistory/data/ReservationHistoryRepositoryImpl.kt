package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.dto.request.GetReservationHistoryRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.remote.ReservationHistoryService
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.ReservationHistoryRepository
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.GetReservationHistoryUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class ReservationHistoryRepositoryImpl @Inject constructor(
    private val reservationHistoryService: ReservationHistoryService
) : ReservationHistoryRepository,ApiExecutor{
    override suspend fun getReservationHistory(getReservationHistoryRequestDto: GetReservationHistoryRequestDto): UiResult<GetReservationHistoryUiModel, ApiErrorModel> {
        val apiResult = execute {
            reservationHistoryService.getReservationHistory(
                userId = getReservationHistoryRequestDto.userId
            )
        }

        return when(apiResult){
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }
}