package com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class AddNewCarRequestDto(
    val userId : Int,
    val plate : String,
    val model : String,
    val fuelTypeId : Int
) : UseCaseParams
