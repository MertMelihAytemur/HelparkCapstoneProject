package com.tr.helpark.helparkcapstoneproject.features.register.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.register.data.dto.request.RegisterRequestDto
import com.tr.helpark.helparkcapstoneproject.features.register.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.register.data.remote.RegisterService
import com.tr.helpark.helparkcapstoneproject.features.register.domain.RegisterRepository
import com.tr.helpark.helparkcapstoneproject.features.register.domain.uimodel.RegisterUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val registerService: RegisterService
) : RegisterRepository, ApiExecutor {

    override suspend fun register(registerRequestDto: RegisterRequestDto): UiResult<RegisterUiModel, ApiErrorModel> {
        val apiResult = execute {
            registerService.register(registerRequestDto)
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