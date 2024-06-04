package com.tr.helpark.helparkcapstoneproject.features.reservation.data.remote

import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.AddReservationRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.response.AddReservationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReservationService {
    @POST(END_POINT_ADD_RESERVATION)
    suspend fun addReservation(
        @Body addReservationRequestDto : AddReservationRequestDto
    ) : Response<AddReservationResponseDto>
    private companion object{
        const val END_POINT_ADD_RESERVATION ="/User/AddReservation"
    }
}