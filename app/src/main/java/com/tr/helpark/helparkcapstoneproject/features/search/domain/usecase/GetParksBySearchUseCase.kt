package com.tr.helpark.helparkcapstoneproject.features.search.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.search.data.dto.request.GetParksBySearchRequestDto
import com.tr.helpark.helparkcapstoneproject.features.search.domain.SearchRepository
import com.tr.helpark.helparkcapstoneproject.features.search.domain.uimodel.GetParksBySearchUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class GetParksBySearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : UseCase<GetParksBySearchUiModel,ApiErrorModel>{
    override suspend fun invoke(params: UseCaseParams?): UiResult<GetParksBySearchUiModel, ApiErrorModel> {
        return searchRepository.getParksBySearch(params as GetParksBySearchRequestDto)
    }
}