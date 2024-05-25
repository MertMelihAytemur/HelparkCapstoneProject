package com.tr.helpark.helparkcapstoneproject.features.home.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModel
import tr.com.helpark.core.domain.UiResult

interface HomeRepository {

    suspend fun getAllParks() : UiResult<GetAllParksUiModel,ApiErrorModel>
}