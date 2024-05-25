package com.tr.helpark.helparkcapstoneproject.features.login.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel.LoginUiModel

data class LoginResponseDto(
    val message: String?
)

fun LoginResponseDto.toDomain() : LoginUiModel{
    return LoginUiModel(
        message = this.message
    )
}