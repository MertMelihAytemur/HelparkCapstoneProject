package com.tr.helpark.helparkcapstoneproject.features.mycars.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.AddNewCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.MyCarsRepository
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.AddCarUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class AddCarUseCase @Inject constructor(
    private val myCarsRepository: MyCarsRepository
) : UseCase<AddCarUiModel, ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<AddCarUiModel, ApiErrorModel> {
        return myCarsRepository.addNewCar(params as AddNewCarRequestDto)
    }
}