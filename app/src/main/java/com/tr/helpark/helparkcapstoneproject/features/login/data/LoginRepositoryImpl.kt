package com.tr.helpark.helparkcapstoneproject.features.login.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.login.data.dto.request.LoginRequestDto
import com.tr.helpark.helparkcapstoneproject.features.login.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.login.data.remote.LoginService
import com.tr.helpark.helparkcapstoneproject.features.login.domain.LoginRepository
import com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel.LoginUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService
) : LoginRepository, ApiExecutor{
    override suspend fun login(loginResponseDto: LoginRequestDto): UiResult<LoginUiModel, ApiErrorModel> {
        val apiResult = execute {
            loginService.login(loginResponseDto)
        }

        return when(apiResult){
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }
}