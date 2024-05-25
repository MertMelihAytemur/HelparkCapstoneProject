package com.tr.helpark.helparkcapstoneproject.features.home.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.home.domain.HomeRepository
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class GetAllParksUseCase @Inject constructor(
    private val homeRepository : HomeRepository
) : UseCase<GetAllParksUiModel, ApiErrorModel>{
    override suspend fun invoke(params: UseCaseParams?): UiResult<GetAllParksUiModel, ApiErrorModel> {
        return homeRepository.getAllParks()
    }
}