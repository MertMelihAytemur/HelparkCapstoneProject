package com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel

import androidx.collection.ArrayMap
import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import java.util.UUID

data class GetAllParksUiModel(
    val parks: List<GetAllParksUiModelItem>,
    val uniqueId : String = UUID.randomUUID().toString()
) : UiModel

data class GetAllParksUiModelItem(
    val capacity: Int?,
    val district: String?,
    val emptyCapacity: Int?,
    val freeTime: Int?,
    val id: Int?,
    val isOpen: Int?,
    val isOpened: Boolean?,
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
    val hire: Float?,
    val id: Int?,
    val monthlyFee: Int?,
    val parkId: Int?,
    val resTime: Int?,
    val tariff: ArrayMap<String, String>?,
    val updateDate: String?,
    val workHours: String?
)

sealed interface GetAllParksApiState {
    object Initial : GetAllParksApiState
    data class Success(val uiModel: GetAllParksUiModel?) : GetAllParksApiState
    data class Error(val error: UiError<ApiErrorModel>) : GetAllParksApiState
}