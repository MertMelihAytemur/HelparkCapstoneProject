package com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.DeleteUserAccountRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.ProfileRepository
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.DeleteUserAccountUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class DeleteUserAccountUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) : UseCase<DeleteUserAccountUiModel, ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<DeleteUserAccountUiModel, ApiErrorModel> {
        return profileRepository.deleteUserAccount(params as DeleteUserAccountRequestDto)
    }
}