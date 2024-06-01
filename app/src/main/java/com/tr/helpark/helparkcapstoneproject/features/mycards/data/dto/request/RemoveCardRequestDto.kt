package com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class RemoveCardRequestDto(
    val userId : Int,
    val cardId : Int
) : UseCaseParams
