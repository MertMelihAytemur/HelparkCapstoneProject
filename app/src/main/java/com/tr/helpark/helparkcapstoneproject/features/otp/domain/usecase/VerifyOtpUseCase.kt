package com.tr.helpark.helparkcapstoneproject.features.otp.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.VerifyOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.OtpVerificationRepository
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.VerifyOtpUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val otpVerificationRepository: OtpVerificationRepository
) : UseCase<VerifyOtpUiModel,ApiErrorModel>{
    override suspend fun invoke(params: UseCaseParams?): UiResult<VerifyOtpUiModel, ApiErrorModel> {
        return otpVerificationRepository.verifyOtp(params as VerifyOtpRequestDto)
    }
}