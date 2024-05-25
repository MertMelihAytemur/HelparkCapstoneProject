package com.tr.helpark.helparkcapstoneproject.features.login.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class LoginRequestDto(
    val phoneNumber : String
) : UseCaseParams
