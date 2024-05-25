package com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel

import tr.com.helpark.core.domain.UiModel

data class GetAllParksUiModel(
    val parks: List<GetAllParksUiModelItem>
) : UiModel

data class GetAllParksUiModelItem(
    val capacity: Int?,
    val district: String?,
    val emptyCapacity: Int?,
    val freeTime: Int?,
    val id: Int?,
    val isOpen: String?,
    val lat: String?,
    val lng: String?,
    val parkDetail: ParkDetailUiModel?,
    val parkName: String?,
    val parkPoint: String?,
    val parkType: String?,
    val state: Int?,
    val workHours: String?
)

data class ParkDetailUiModel(
    val address: String?,
    val areaPolygon: String?,
    val district: String?,
    val id: Int?,
    val monthlyFee: Int?,
    val parkId: Int?,
    val tariff: String?,
    val updateDate: String?,
    val workHours: String?
)