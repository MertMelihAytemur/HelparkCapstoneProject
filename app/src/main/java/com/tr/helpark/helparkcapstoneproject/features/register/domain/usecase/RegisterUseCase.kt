package com.tr.helpark.helparkcapstoneproject.features.register.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.register.data.dto.request.RegisterRequestDto
import com.tr.helpark.helparkcapstoneproject.features.register.domain.RegisterRepository
import com.tr.helpark.helparkcapstoneproject.features.register.domain.uimodel.RegisterUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: RegisterRepository
) : UseCase<RegisterUiModel,ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<RegisterUiModel, ApiErrorModel> {
        return repository.register(params as RegisterRequestDto)
    }

}