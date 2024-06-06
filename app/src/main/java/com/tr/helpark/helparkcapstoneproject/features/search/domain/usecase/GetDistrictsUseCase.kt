package com.tr.helpark.helparkcapstoneproject.features.search.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.search.domain.SearchRepository
import com.tr.helpark.helparkcapstoneproject.features.search.domain.uimodel.GetDistrictsUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class GetDistrictsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : UseCase<GetDistrictsUiModel,ApiErrorModel>{
    override suspend fun invoke(params: UseCaseParams?): UiResult<GetDistrictsUiModel, ApiErrorModel> {
        return searchRepository.getDistricts()
    }
}