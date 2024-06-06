package com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel

import android.os.Parcelable
import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import kotlinx.parcelize.Parcelize
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel
import java.util.UUID

data class GetFavoritesUiModel(
    val parks: List<GetFavoritesUiModelItem>,
    val uniqueId : String = UUID.randomUUID().toString()
): UiModel


@Parcelize
data class GetFavoritesUiModelItem(
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
) : Parcelable

@Parcelize
data class ParkDetailUiModel(
    val address: String?,
    val areaPolygon: String?,
    val district: String?,
    val hire: Double?,
    val id: Int?,
    val monthlyFee: Int?,
    val parkId: Int?,
    val resTime: Int?,
    val tariff: String?,
    val updateDate: String?,
    val workHours: String?
) : Parcelable

sealed interface GetFavoritesApiState{
    object Initial : GetFavoritesApiState
    data class Success(val uiModel : GetFavoritesUiModel?) : GetFavoritesApiState
    data class Error(val error : UiError<ApiErrorModel>) : GetFavoritesApiState
}