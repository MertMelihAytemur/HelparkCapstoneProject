package com.tr.helpark.helparkcapstoneproject.features.login.data.remote

import com.tr.helpark.helparkcapstoneproject.features.login.data.dto.request.LoginRequestDto
import com.tr.helpark.helparkcapstoneproject.features.login.data.dto.response.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST(END_POINT_LOGIN)
    suspend fun login(@Body loginRequestDto: LoginRequestDto): Response<LoginResponseDto>

    private companion object{
        const val END_POINT_LOGIN = "Auth/verifyPhoneNumber"
    }
}