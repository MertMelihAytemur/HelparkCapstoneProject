package com.tr.helpark.helparkcapstoneproject.features.home.data.dto.response


import androidx.collection.ArrayMap
import com.google.gson.annotations.SerializedName
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModel
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.ParkDetailUiModel

class GetAllParksResponseDto : ArrayList<GetAllParksResponseDtoItem>()


data class GetAllParksResponseDtoItem(
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
    @SerializedName("hire")
    val hire: Float?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("monthlyFee")
    val monthlyFee: Int?,
    @SerializedName("parkId")
    val parkId: Int?,
    @SerializedName("resTime")
    val resTime: Int?,
    @SerializedName("tariff")
    val tariff: String?,
    @SerializedName("updateDate")
    val updateDate: String?,
    @SerializedName("workHours")
    val workHours: String?
)


fun GetAllParksResponseDto.toDomain(): GetAllParksUiModel {
    return GetAllParksUiModel(
        parks = this.map { it.toDomain() }
    )
}

fun GetAllParksResponseDtoItem.toDomain(): GetAllParksUiModelItem {
    return GetAllParksUiModelItem(
        capacity = this.capacity,
        district = this.district,
        emptyCapacity = this.emptyCapacity,
        freeTime = this.freeTime,
        id = this.id,
        isOpen = this.isOpen,
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

fun ParkDetail.toDomain(): ParkDetailUiModel {
    return ParkDetailUiModel(
        address = this.address,
        areaPolygon = this.areaPolygon,
        district = this.district,
        hire = this.hire,
        id = this.id,
        monthlyFee = this.monthlyFee,
        parkId = this.parkId,
        resTime = this.resTime,
        tariff = parseFeeScheduleToUiModel(this.tariff),
        updateDate = this.updateDate,
        workHours = this.workHours
    )
}

fun parseFeeScheduleToUiModel(schedule: String?): ArrayMap<String, String>? {
    if (schedule.isNullOrEmpty()) return null

    val scheduleMap = ArrayMap<String, String>()
    // Split entries by ';' and handle possible spaces around it
    val scheduleEntries = schedule.split(";").map { it.trim() }

    scheduleEntries.forEach { entry ->
        // Split each entry by ':' and handle possible spaces around it
        val parts = entry.split(":").map { it.trim() }
        if (parts.size == 2) {
            val hoursRange = parts[0]
            val price = parts[1]
            scheduleMap[hoursRange] = price
        }
    }
    return scheduleMap
}