package com.tr.helpark.helparkcapstoneproject.features.favorites.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.favorites.data.dto.request.GetFavoritesRequestDto
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.FavoriteRepository
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : UseCase<GetFavoritesUiModel,ApiErrorModel>{
    override suspend fun invoke(params: UseCaseParams?): UiResult<GetFavoritesUiModel, ApiErrorModel> {
        return favoriteRepository.getFavorites(params as GetFavoritesRequestDto)
    }
}