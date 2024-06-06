package com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.AddCardUiModel
import java.util.UUID

data class AddCardResponseDto(
    val message : String?,
    val uniqueId : String? = UUID.randomUUID().toString()
)

fun AddCardResponseDto.toDomain() : AddCardUiModel {
    return AddCardUiModel(
        message = this.message
    )
}