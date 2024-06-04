package com.tr.helpark.helparkcapstoneproject.features.reservation.data.remote

import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.AddReservationRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.response.AddReservationResponseDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.response.CancelReservationResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ReservationService {
    @POST(END_POINT_ADD_RESERVATION)
    suspend fun addReservation(
        @Body addReservationRequestDto : AddReservationRequestDto
    ) : Response<AddReservationResponseDto>

    @POST(END_POINT_CANCEL_RESERVATION)
    suspend fun cancelReservation(
        @Query("rezId") rezId : Int
    ) : Response<CancelReservationResponseDto>

    private companion object{
        const val END_POINT_ADD_RESERVATION ="/Rezervation/AddReservation"
        const val END_POINT_CANCEL_RESERVATION = "/Rezervation/CancelRezervationId"
    }

}