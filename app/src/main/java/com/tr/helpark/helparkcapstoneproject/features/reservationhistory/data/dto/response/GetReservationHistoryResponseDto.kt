package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.dto.response


import com.google.gson.annotations.SerializedName
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.GetReservationHistoryUiModel
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.ReservationHistoryItemUiModel


data class GetReservationHistoryResponseDto(
    @SerializedName("rezervations")
    val rezervations: List<RezervationHistoryItemDto>?
)

data class RezervationHistoryItemDto(
    @SerializedName("carPlateId")
    val carPlateId: Int?,
    @SerializedName("hire")
    val hire: Float?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("parkId")
    val parkId: Int?,
    @SerializedName("resDate")
    val resDate: String?,
    @SerializedName("resTime")
    val resTime: Int?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("parkName")
    val parkName : String? = null,
    @SerializedName("lat")
    val latitude : String? = null,
    @SerializedName("lng")
    val longitude : String? = null,
)

fun GetReservationHistoryResponseDto.toDomain() : GetReservationHistoryUiModel {
    return GetReservationHistoryUiModel(
        reservations = rezervations?.map { it.toDomain() } ?: emptyList()
    )
}

private fun RezervationHistoryItemDto.toDomain() : ReservationHistoryItemUiModel {
    return ReservationHistoryItemUiModel(
        carPlateId = carPlateId,
        hire = hire,
        id = id,
        parkId = parkId,
        resDate = resDate,
        resTime = resTime,
        status = status,
        userId = userId,
        parkName = parkName,
        latitude = latitude,
        longitude = longitude
    )
}