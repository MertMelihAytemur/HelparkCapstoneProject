package com.tr.helpark.helparkcapstoneproject.features.search.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModel
import com.tr.helpark.helparkcapstoneproject.features.search.data.dto.request.GetParksBySearchRequestDto
import com.tr.helpark.helparkcapstoneproject.features.search.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.search.data.remote.SearchService
import com.tr.helpark.helparkcapstoneproject.features.search.domain.SearchRepository
import com.tr.helpark.helparkcapstoneproject.features.search.domain.uimodel.GetDistrictsUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchService: SearchService
)  : SearchRepository, ApiExecutor {
    override suspend fun getParksBySearch(getParksBySearchRequestDto: GetParksBySearchRequestDto): UiResult<GetAllParksUiModel, ApiErrorModel> {
        val apiResult = execute {
            searchService.getParksBySearch(
                district = getParksBySearchRequestDto.district,
                latitude = getParksBySearchRequestDto.latitude,
                longitude = getParksBySearchRequestDto.longitude,
                radius = getParksBySearchRequestDto.radius,
            )
        }

        return when (apiResult) {
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }

        }
    }

    override suspend fun getDistricts(): UiResult<GetDistrictsUiModel, ApiErrorModel> {
        val apiResult = execute {
            searchService.getDistricts()
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