package com.tr.helpark.helparkcapstoneproject.features.otp.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.SendOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.VerifyOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.SendOtpUiModel
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.VerifyOtpUiModel
import tr.com.helpark.core.domain.UiResult

interface OtpVerificationRepository {

    suspend fun verifyOtp(verifyOtpRequestDto: VerifyOtpRequestDto): UiResult<VerifyOtpUiModel, ApiErrorModel>
    suspend fun sendOtp(sendOtpRequestDto: SendOtpRequestDto): UiResult<SendOtpUiModel, ApiErrorModel>
}