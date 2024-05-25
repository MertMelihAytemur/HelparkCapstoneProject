package com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class VerifyOtpRequestDto(
    val phoneNumber: String,
    val otp: String
) : UseCaseParams
