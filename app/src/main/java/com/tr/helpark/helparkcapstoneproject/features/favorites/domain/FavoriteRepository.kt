package com.tr.helpark.helparkcapstoneproject.features.favorites.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.favorites.data.dto.request.GetFavoritesRequestDto
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModel
import tr.com.helpark.core.domain.UiResult

interface FavoriteRepository {

    suspend fun getFavorites(getFavoritesRequestDto: GetFavoritesRequestDto): UiResult<GetFavoritesUiModel,ApiErrorModel>
}