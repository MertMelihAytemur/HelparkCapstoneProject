package com.tr.helpark.helparkcapstoneproject.features.favorites.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class GetFavoritesRequestDto(
    val userId: String
) : UseCaseParams
