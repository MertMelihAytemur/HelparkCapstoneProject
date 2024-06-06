package com.tr.helpark.helparkcapstoneproject.features.register.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.register.domain.uimodel.RegisterUiModel
import java.util.UUID

data class RegisterResponseDto(
    val message : String?,
    val uniqueId : String? = UUID.randomUUID().toString()
)

fun RegisterResponseDto.toDomain() = RegisterUiModel(
    message = this.message
)