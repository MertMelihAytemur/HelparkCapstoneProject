package com.tr.helpark.helparkcapstoneproject.features.favorites.data.remote

import com.tr.helpark.helparkcapstoneproject.features.favorites.data.dto.response.FavoriteResponseDto
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface FavoriteService {

    @POST(END_POINT_GET_FAVORITES)
    suspend fun getFavorites(
        @Query("UserId") userId: String
    ) : Response<FavoriteResponseDto>

    companion object{
        const val END_POINT_GET_FAVORITES = "/User/GetFavourite"
    }
}