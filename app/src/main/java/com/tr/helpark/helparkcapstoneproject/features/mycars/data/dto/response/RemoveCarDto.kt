package com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.RemoveCarUiModel

data class RemoveCarDto(
    val message : String?
)

fun RemoveCarDto.toDomain() : RemoveCarUiModel{
    return RemoveCarUiModel(
        message = this.message
    )
}