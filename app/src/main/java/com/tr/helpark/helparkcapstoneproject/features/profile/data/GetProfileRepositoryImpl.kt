package com.tr.helpark.helparkcapstoneproject.features.profile.data

import com.helpark.helpark.common.utils.preferences.PreferencesKeys.KEY_USER_PROFILE
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.profile.data.remote.ProfileService
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.ProfileRepository
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class GetProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService,
    private val preferencesManager: PreferencesManager
) : ProfileRepository,ApiExecutor {
    override suspend fun getProfile(getProfileRequestDto: GetProfileRequestDto): UiResult<GetProfileUiModel, ApiErrorModel> {
        val apiResult = execute {
            profileService.getProfile(getProfileRequestDto.userId)
        }

        return when(apiResult){
            is ApiResult.Success -> {
                val uiModel = apiResult.response?.toDomain()

                uiModel?.let {profile ->
                    preferencesManager.putModel(KEY_USER_PROFILE,profile)
                }

                UiResult.Success(uiModel)
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }
}