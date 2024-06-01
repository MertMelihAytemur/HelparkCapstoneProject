package com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.response


import com.google.gson.annotations.SerializedName

class GetFuelsDto : ArrayList<GetFuelsDtoItem>()

data class GetFuelsDtoItem(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?
)
