package com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.otp.domain.uimodel.SendOtpUiModel

data class SendOtpResponseDto(
    val message: String?
)

fun SendOtpResponseDto.toDomain() : SendOtpUiModel {
    return SendOtpUiModel(
        message = message
    )
}