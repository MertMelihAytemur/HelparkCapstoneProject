package com.tr.helpark.helparkcapstoneproject.features.search.domain.uimodel

import tr.com.helpark.core.domain.UiModel

data class GetDistrictsUiModel(
    val districts: List<GetDistrictsItemUiModel>?
) : UiModel

data class GetDistrictsItemUiModel(
    val lat: String?,
    val lng: String?,
    val district: String?
) : UiModel
