package com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.AddCarUiModel

data class AddNewCarDto(
    val message : String?
)

fun AddNewCarDto.toUiModel() = AddCarUiModel(
    message = message
)