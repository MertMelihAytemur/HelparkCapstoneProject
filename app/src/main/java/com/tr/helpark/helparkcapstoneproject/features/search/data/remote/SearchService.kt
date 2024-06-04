package com.tr.helpark.helparkcapstoneproject.features.search.data.remote

import com.tr.helpark.helparkcapstoneproject.features.search.data.dto.response.GetParksBySearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET(END_POINT_SEARCH)
    suspend fun getParksBySearch(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double
    ): Response<GetParksBySearchResponseDto>


    private companion object{
        const val END_POINT_SEARCH = "/Park/GetParksInRadius"
    }
}