package com.tr.helpark.helparkcapstoneproject.features.search.data.remote

import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.response.GetAllParksResponseDto
import com.tr.helpark.helparkcapstoneproject.features.search.data.dto.response.GetDistrictsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET(END_POINT_SEARCH)
    suspend fun getParksBySearch(
        @Query("district") district: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("radius") radius: Double
    ): Response<GetAllParksResponseDto>


    @GET(END_POINT_GET_DISTRICTS)
    suspend fun getDistricts(): Response<GetDistrictsResponseDto>

    private companion object{
        const val END_POINT_SEARCH = "/Park/GetParksInRadius"
        const val END_POINT_GET_DISTRICTS = "/Park/GetDistrict"
    }
}