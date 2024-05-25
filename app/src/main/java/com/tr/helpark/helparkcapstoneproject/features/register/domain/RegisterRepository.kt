package com.tr.helpark.helparkcapstoneproject.features.register.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.register.data.dto.request.RegisterRequestDto
import com.tr.helpark.helparkcapstoneproject.features.register.domain.uimodel.RegisterUiModel
import tr.com.helpark.core.domain.UiResult

interface RegisterRepository {

    suspend fun register(
        registerRequestDto: RegisterRequestDto
    ) : UiResult<RegisterUiModel, ApiErrorModel>
}