package com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class RemoveCarRequestDto(
    val plate : String
) : UseCaseParams
