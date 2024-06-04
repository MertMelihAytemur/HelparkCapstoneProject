package com.tr.helpark.helparkcapstoneproject.features.home.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.ToggleFavoriteUiModel

data class ToggleFavoriteParkDto(
    val message: Boolean
)

fun ToggleFavoriteParkDto.toDomain() = ToggleFavoriteUiModel(
    isFavorite = message
)