package com.tr.helpark.helparkcapstoneproject.features.mycars.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.RemoveCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.MyCarsRepository
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.RemoveCarUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class RemoveCarUseCase @Inject constructor(
    private val myCarsRepository: MyCarsRepository
) : UseCase<RemoveCarUiModel, ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<RemoveCarUiModel, ApiErrorModel> {
        return myCarsRepository.removeCar(params as RemoveCarRequestDto)
    }
}