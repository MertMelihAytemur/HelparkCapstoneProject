package com.tr.helpark.helparkcapstoneproject.features.mycars.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.AddNewCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.RemoveCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.response.toUiModel
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.remote.MyCarsService
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.MyCarsRepository
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.AddCarUiModel
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.RemoveCarUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class MyCarsRepositoryImpl @Inject constructor(
    private val myCarsService: MyCarsService
) : MyCarsRepository, ApiExecutor {
    override suspend fun addNewCar(addNewCarRequestDto: AddNewCarRequestDto): UiResult<AddCarUiModel, ApiErrorModel> {
        val apiResult = execute {
            myCarsService.addPlate(
                addNewCarRequestDto
            )
        }

        return when (apiResult) {
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toUiModel())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }

    override suspend fun removeCar(removeCarRequestDto: RemoveCarRequestDto): UiResult<RemoveCarUiModel, ApiErrorModel> {
        val apiResult = execute {
            myCarsService.removePlate(
                removeCarRequestDto.plate,
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
}