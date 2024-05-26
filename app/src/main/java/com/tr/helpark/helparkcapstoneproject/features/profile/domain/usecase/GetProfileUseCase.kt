package com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.ProfileRepository
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : UseCase<GetProfileUiModel,ApiErrorModel>{
    override suspend fun invoke(params: UseCaseParams?): UiResult<GetProfileUiModel, ApiErrorModel> {
        return profileRepository.getProfile(params as GetProfileRequestDto)
    }
}