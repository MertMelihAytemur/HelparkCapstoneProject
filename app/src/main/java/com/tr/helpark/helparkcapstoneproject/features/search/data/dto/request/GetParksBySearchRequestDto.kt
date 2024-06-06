package com.tr.helpark.helparkcapstoneproject.features.search.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class GetParksBySearchRequestDto(
    val district: String,
    val latitude: String,
    val longitude: String,
    val radius: Double
) : UseCaseParams
