package com.tr.helpark.helparkcapstoneproject.features.otp.data.remote

import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.SendOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.VerifyOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.response.SendOtpResponseDto
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.response.VerifyOtpResponseDto
import retrofit2.Response
import retrofit2.http.POST

interface OtpVerificationService {

    @POST(END_POINT_VERIFY_OTP)
    suspend fun verifyOtp(verifyOtpRequestDto: VerifyOtpRequestDto): Response<VerifyOtpResponseDto>

    @POST(END_POINT_SEND_OTP)
    suspend fun sendOtp(sendOtpRequestDto: SendOtpRequestDto): Response<SendOtpResponseDto>

    private companion object{
        const val END_POINT_VERIFY_OTP = "/api/OTPVerification/Verify"
        const val END_POINT_SEND_OTP = "/api/GenerateOtp"
    }
}