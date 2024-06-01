package com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.AddBalanceRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.ProfileRepository
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.AddBalanceUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import tr.com.helpark.core.util.logD
import javax.inject.Inject

class AddBalanceUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : UseCase<AddBalanceUiModel, ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<AddBalanceUiModel, ApiErrorModel> {
        logD("addBalanceUseCase 4")
        return profileRepository.addBalance(params as AddBalanceRequestDto)
    }
}