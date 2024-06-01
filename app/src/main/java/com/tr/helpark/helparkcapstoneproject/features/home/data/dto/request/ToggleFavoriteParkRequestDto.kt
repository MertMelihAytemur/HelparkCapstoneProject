package com.tr.helpark.helparkcapstoneproject.features.home.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class ToggleFavoriteParkRequestDto(
    val userId : Int,
    val parkId : Int
) : UseCaseParams
