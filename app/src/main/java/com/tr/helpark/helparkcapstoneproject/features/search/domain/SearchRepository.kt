package com.tr.helpark.helparkcapstoneproject.features.search.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.search.data.dto.request.GetParksBySearchRequestDto
import com.tr.helpark.helparkcapstoneproject.features.search.domain.uimodel.GetParksBySearchUiModel
import tr.com.helpark.core.domain.UiResult

interface SearchRepository {

    suspend fun getParksBySearch(
        getParksBySearchRequestDto: GetParksBySearchRequestDto
    ): UiResult<GetParksBySearchUiModel, ApiErrorModel>
}