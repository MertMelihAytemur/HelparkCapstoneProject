package com.tr.helpark.helparkcapstoneproject.features.profile.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import tr.com.helpark.core.domain.UiResult

interface ProfileRepository {
    suspend fun getProfile(getProfileRequestDto: GetProfileRequestDto) : UiResult<GetProfileUiModel,ApiErrorModel>
}