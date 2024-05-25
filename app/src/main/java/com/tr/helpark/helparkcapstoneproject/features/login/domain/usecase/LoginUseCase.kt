package com.tr.helpark.helparkcapstoneproject.features.login.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.login.data.dto.request.LoginRequestDto
import com.tr.helpark.helparkcapstoneproject.features.login.domain.LoginRepository
import com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel.LoginUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) : UseCase<LoginUiModel,ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<LoginUiModel, ApiErrorModel> {
        return loginRepository.login(params as LoginRequestDto)
    }
}