package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.remote

import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.dto.response.GetReservationHistoryResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReservationHistoryService {

    @GET(END_POINT_RESERVATION_HISTORY)
    suspend fun getReservationHistory(
        @Path("userId") userId: Int
    ) : Response<GetReservationHistoryResponseDto>

    private companion object{
        const val END_POINT_RESERVATION_HISTORY = "/Rezervation/{userId}"
    }
}