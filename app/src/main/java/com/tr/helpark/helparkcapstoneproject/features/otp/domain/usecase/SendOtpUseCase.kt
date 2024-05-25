package com.tr.helpark.helparkcapstoneproject.features.otp.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request.SendOtpRequestDto
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.OtpVerificationRepository
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.SendOtpUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val otpVerificationRepository: OtpVerificationRepository
) : UseCase<SendOtpUiModel,ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<SendOtpUiModel, ApiErrorModel> {
        return otpVerificationRepository.sendOtp(params as SendOtpRequestDto)
    }
}