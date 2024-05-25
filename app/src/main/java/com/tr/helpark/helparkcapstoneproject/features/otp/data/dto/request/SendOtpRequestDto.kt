package com.tr.helpark.helparkcapstoneproject.features.otp.data.dto.request

import com.google.gson.annotations.SerializedName
import tr.com.helpark.core.domain.UseCaseParams

data class SendOtpRequestDto(
    @SerializedName("PhoneNumber")
    val phoneNumber: String
) : UseCaseParams
