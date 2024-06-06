package com.tr.helpark.helparkcapstoneproject.features.search.data.dto.response

import com.google.gson.annotations.SerializedName
import com.tr.helpark.helparkcapstoneproject.features.search.domain.uimodel.GetDistrictsItemUiModel
import com.tr.helpark.helparkcapstoneproject.features.search.domain.uimodel.GetDistrictsUiModel

class GetDistrictsResponseDto : ArrayList<GetDistrictsResponseItemDto>()


data class GetDistrictsResponseItemDto(
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lng")
    val lng: String?,
    @SerializedName("sehir")
    val city: String?,
    @SerializedName("semt")
    val district: String?
)

fun GetDistrictsResponseDto.toDomain(): GetDistrictsUiModel {
    return GetDistrictsUiModel(
        districts = this.map { item ->
            GetDistrictsItemUiModel(
                lat = item.lat,
                lng = item.lng,
                district = item.district
            )
        }
    )
}