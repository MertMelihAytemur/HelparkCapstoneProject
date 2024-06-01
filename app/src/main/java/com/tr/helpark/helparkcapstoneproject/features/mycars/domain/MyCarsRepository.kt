package com.tr.helpark.helparkcapstoneproject.features.mycars.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.AddNewCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.RemoveCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.AddCarUiModel
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.RemoveCarUiModel
import tr.com.helpark.core.domain.UiResult

interface MyCarsRepository {

    suspend fun addNewCar(
        addNewCarRequestDto: AddNewCarRequestDto
    ) : UiResult<AddCarUiModel,ApiErrorModel>

    suspend fun removeCar(
        removeCarRequestDto: RemoveCarRequestDto
    ) : UiResult<RemoveCarUiModel,ApiErrorModel>
}