package com.tr.helpark.helparkcapstoneproject.features.favorites.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.favorites.data.dto.request.GetFavoritesRequestDto
import com.tr.helpark.helparkcapstoneproject.features.favorites.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.favorites.data.remote.FavoriteService
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.FavoriteRepository
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteService: FavoriteService
)  : FavoriteRepository,ApiExecutor{
    override suspend fun getFavorites(getFavoritesRequestDto: GetFavoritesRequestDto): UiResult<GetFavoritesUiModel, ApiErrorModel> {
        val apiResult = execute {
            favoriteService.getFavorites(getFavoritesRequestDto.userId)
        }

        return when(apiResult){
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }
}