package com.tr.helpark.helparkcapstoneproject.features.home.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.home.data.remote.HomeService
import com.tr.helpark.helparkcapstoneproject.features.home.domain.HomeRepository
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService
) : HomeRepository, ApiExecutor {
    override suspend fun getAllParks(): UiResult<GetAllParksUiModel, ApiErrorModel> {
        val apiResult = execute {
            homeService.getAllParks()
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
}