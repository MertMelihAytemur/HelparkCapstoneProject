package com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.DeleteUserAccountUiModel

data class DeleteUserAccountDto(
    val message: String
)

fun DeleteUserAccountDto.toDomain() = DeleteUserAccountUiModel(
    message = message
)
