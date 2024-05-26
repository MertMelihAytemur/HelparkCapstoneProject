package com.tr.helpark.helparkcapstoneproject.features.profile.data.remote

import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.response.GetProfileResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {

    @GET(END_POINT_GET_PROFILE)
    suspend fun getProfile(
        @Path("userId") userId: String
    ) : Response<GetProfileResponseDto>

    private companion object{
        const val END_POINT_GET_PROFILE = "/api/User/GetAllDataFromUser/{userId}"
    }
}