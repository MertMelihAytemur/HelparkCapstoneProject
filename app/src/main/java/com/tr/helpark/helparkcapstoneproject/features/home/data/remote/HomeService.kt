package com.tr.helpark.helparkcapstoneproject.features.home.data.remote

import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.response.GetAllParksResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface HomeService {
    @GET(END_POINT_GET_ALL_PARKS)
    suspend fun getAllParks() : Response<GetAllParksResponseDto>

    companion object{
        const val END_POINT_GET_ALL_PARKS = "/Park/ParkList"
    }
}