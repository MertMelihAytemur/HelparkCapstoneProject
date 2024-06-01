package com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.RemoveCardUiModel

data class RemoveCardResponseDto(
    val message : String?
)

fun RemoveCardResponseDto.toDomain() : RemoveCardUiModel {
    return RemoveCardUiModel(
        message = this.message
    )
}