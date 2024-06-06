package com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.RemoveCardUiModel
import java.util.UUID

data class RemoveCardResponseDto(
    val message : String?,
    val uniqueId : String? = UUID.randomUUID().toString()
)

fun RemoveCardResponseDto.toDomain() : RemoveCardUiModel {
    return RemoveCardUiModel(
        message = this.message
    )
}