package com.tr.helpark.helparkcapstoneproject.features.home.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.request.ToggleFavoriteParkRequestDto
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModel
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.ToggleFavoriteUiModel
import tr.com.helpark.core.domain.UiResult

interface HomeRepository {
    suspend fun getAllParks(): UiResult<GetAllParksUiModel, ApiErrorModel>
    suspend fun toggleFavoritePark(
        toggleFavoriteParkRequestDto: ToggleFavoriteParkRequestDto
    ): UiResult<ToggleFavoriteUiModel, ApiErrorModel>
}