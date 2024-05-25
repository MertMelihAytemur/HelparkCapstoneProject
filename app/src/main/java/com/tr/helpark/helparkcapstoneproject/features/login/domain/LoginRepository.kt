package com.tr.helpark.helparkcapstoneproject.features.login.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.login.data.dto.request.LoginRequestDto
import com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel.LoginUiModel
import tr.com.helpark.core.domain.UiResult

interface LoginRepository {
    suspend fun login(loginResponseDto: LoginRequestDto) : UiResult<LoginUiModel,ApiErrorModel>
}