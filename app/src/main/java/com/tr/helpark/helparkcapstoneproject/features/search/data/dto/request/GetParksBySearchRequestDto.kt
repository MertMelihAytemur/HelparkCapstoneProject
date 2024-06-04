package com.tr.helpark.helparkcapstoneproject.features.search.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class GetParksBySearchRequestDto(
    val latitude: Double,
    val longitude: Double,
    val radius: Double
) : UseCaseParams
