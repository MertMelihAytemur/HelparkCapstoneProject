package com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.VerifyOtpUiModel

data class VerifyOtpResponseDto(
    val message : String?
)

fun VerifyOtpResponseDto.toDomain() : VerifyOtpUiModel {
    return VerifyOtpUiModel(
        message = message
    )
}