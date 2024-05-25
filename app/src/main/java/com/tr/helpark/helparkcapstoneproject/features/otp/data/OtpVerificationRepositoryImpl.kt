package com.tr.helpark.helparkcapstoneproject.features.otp.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.SendOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.VerifyOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.otp.data.remote.OtpVerificationService
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.OtpVerificationRepository
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.SendOtpUiModel
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.VerifyOtpUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class OtpVerificationRepositoryImpl @Inject constructor(
    private val otpVerificationService: OtpVerificationService
) : OtpVerificationRepository,ApiExecutor
{
    override suspend fun sendOtp(sendOtpRequestDto: SendOtpRequestDto): UiResult<SendOtpUiModel, ApiErrorModel> {
        val apiResult = execute {
            otpVerificationService.sendOtp(sendOtpRequestDto)
        }

        return when(apiResult){
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }

    override suspend fun verifyOtp(verifyOtpRequestDto: VerifyOtpRequestDto): UiResult<VerifyOtpUiModel, ApiErrorModel> {
        val apiResult = execute {
            otpVerificationService.verifyOtp(verifyOtpRequestDto)
        }

        return when(apiResult){
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }
}