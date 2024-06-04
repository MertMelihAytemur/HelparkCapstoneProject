package com.tr.helpark.helparkcapstoneproject.features.home.data.remote

import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.request.ToggleFavoriteParkRequestDto
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.response.GetAllParksResponseDto
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.response.ToggleFavoriteParkDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeService {
    @GET(END_POINT_GET_ALL_PARKS)
    suspend fun getAllParks() : Response<GetAllParksResponseDto>

    @POST(END_POINT_TOGGLE_FAVORITE_PARK)
    suspend fun toggleFavoritePark(
        @Body toggleFavoriteParkRequestDto: ToggleFavoriteParkRequestDto
    ) : Response<ToggleFavoriteParkDto>

    companion object{
        const val END_POINT_GET_ALL_PARKS = "/Park/ParkList"
        const val END_POINT_TOGGLE_FAVORITE_PARK = "/User/AddOrRemoveFavourite"
    }
}