package com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.AddCardUiModel

data class AddCardResponseDto(
    val message : String?
)

fun AddCardResponseDto.toDomain() : AddCardUiModel {
    return AddCardUiModel(
        message = this.message
    )
}