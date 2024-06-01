package com.tr.helpark.helparkcapstoneproject.features.home.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.request.ToggleFavoriteParkRequestDto
import com.tr.helpark.helparkcapstoneproject.features.home.domain.HomeRepository
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.ToggleFavoriteUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) : UseCase<ToggleFavoriteUiModel, ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<ToggleFavoriteUiModel, ApiErrorModel> {
        return homeRepository.toggleFavoritePark(params as ToggleFavoriteParkRequestDto)
    }
}