package com.tr.helpark.helparkcapstoneproject.features.register.data.remote

import com.tr.helpark.helparkcapstoneproject.features.register.data.dto.request.RegisterRequestDto
import com.tr.helpark.helparkcapstoneproject.features.register.data.dto.response.RegisterResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {

    @POST(END_POINT_REGISTER)
    suspend fun register(
        @Body registerRequest: RegisterRequestDto
    ): Response<RegisterResponseDto>

    private companion object{
        const val END_POINT_REGISTER = "/Auth/register"
    }
}