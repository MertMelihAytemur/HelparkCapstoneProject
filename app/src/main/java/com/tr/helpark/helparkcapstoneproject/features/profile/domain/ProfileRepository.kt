package com.tr.helpark.helparkcapstoneproject.features.profile.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.AddBalanceRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.DeleteUserAccountRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.AddBalanceUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.DeleteUserAccountUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import tr.com.helpark.core.domain.UiResult

interface ProfileRepository {
    suspend fun getProfile(
        getProfileRequestDto: GetProfileRequestDto
    ): UiResult<GetProfileUiModel, ApiErrorModel>

    suspend fun addBalance(
        addBalanceRequestDto: AddBalanceRequestDto
    ): UiResult<AddBalanceUiModel, ApiErrorModel>

    suspend fun deleteUserAccount(
        deleteUserAccountRequestDto: DeleteUserAccountRequestDto
    ): UiResult<DeleteUserAccountUiModel, ApiErrorModel>

}