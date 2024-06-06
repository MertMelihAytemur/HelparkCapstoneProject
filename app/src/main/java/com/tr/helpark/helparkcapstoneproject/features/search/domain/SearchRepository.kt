package com.tr.helpark.helparkcapstoneproject.features.search.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModel
import com.tr.helpark.helparkcapstoneproject.features.search.data.dto.request.GetParksBySearchRequestDto
import com.tr.helpark.helparkcapstoneproject.features.search.domain.uimodel.GetDistrictsUiModel
import tr.com.helpark.core.domain.UiResult

interface SearchRepository {

    suspend fun getParksBySearch(
        getParksBySearchRequestDto: GetParksBySearchRequestDto
    ): UiResult<GetAllParksUiModel, ApiErrorModel>

    suspend fun getDistricts(): UiResult<GetDistrictsUiModel, ApiErrorModel>
}