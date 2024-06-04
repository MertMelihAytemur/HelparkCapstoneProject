package com.tr.helpark.helparkcapstoneproject.features.favorites.data.dto.response


import com.google.gson.annotations.SerializedName
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModel
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModelItem

class FavoriteResponseDto : ArrayList<FavoriteResponseDtoItem>()

data class FavoriteResponseDtoItem(
    @SerializedName("capacity")
    val capacity: Int?,
    @SerializedName("district")
    val district: String?,
    @SerializedName("emptyCapacity")
    val emptyCapacity: Int?,
    @SerializedName("freeTime")
    val freeTime: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("isOpen")
    val isOpen: Int?,
    @SerializedName("isOpened")
    val isOpened: Boolean?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lng")
    val lng: String?,
    @SerializedName("parkDetail")
    val parkDetail: ParkDetail?,
    @SerializedName("parkName")
    val parkName: String?,
    @SerializedName("parkPoint")
    val parkPoint: String?,
    @SerializedName("parkType")
    val parkType: String?,
    @SerializedName("state")
    val state: Int?,
    @SerializedName("workHours")
    val workHours: String?
)


data class ParkDetail(
    @SerializedName("address")
    val address: String?,
    @SerializedName("areaPolygon")
    val areaPolygon: String?,
    @SerializedName("district")
    val district: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("monthlyFee")
    val monthlyFee: Int?,
    @SerializedName("parkId")
    val parkId: Int?,
    @SerializedName("tariff")
    val tariff: String?,
    @SerializedName("updateDate")
    val updateDate: String?,
    @SerializedName("workHours")
    val workHours: String?
)


fun FavoriteResponseDto.toDomain(): GetFavoritesUiModel {
    return GetFavoritesUiModel(
        parks = this.map { it.toDomain() }
    )
}

private fun FavoriteResponseDtoItem.toDomain(): GetFavoritesUiModelItem {
    return GetFavoritesUiModelItem(
        capacity = this.capacity,
        district = this.district,
        emptyCapacity = this.emptyCapacity,
        freeTime = this.freeTime,
        id = this.id,
        isOpened = this.isOpened,
        lat = this.lat,
        lng = this.lng,
        parkDetail = this.parkDetail?.toDomain(),
        parkName = this.parkName,
        parkPoint = this.parkPoint,
        parkType = this.parkType,
        state = this.state,
        workHours = this.workHours
    )
}

private fun ParkDetail.toDomain(): com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.ParkDetailUiModel {
    return com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.ParkDetailUiModel(
        address = this.address,
        areaPolygon = this.areaPolygon,
        district = this.district,
        id = this.id,
        monthlyFee = this.monthlyFee,
        parkId = this.parkId,
        tariff = this.tariff,
        updateDate = this.updateDate,
        workHours = this.workHours
    )
}