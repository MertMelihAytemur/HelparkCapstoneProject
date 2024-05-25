package com.tr.helpark.helparkcapstoneproject.features.register.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.register.domain.uimodel.RegisterUiModel

data class RegisterResponseDto(
    val message : String?
)

fun RegisterResponseDto.toDomain() = RegisterUiModel(
    message = this.message
)