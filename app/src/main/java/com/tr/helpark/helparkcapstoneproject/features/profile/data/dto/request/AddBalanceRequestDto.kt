package com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import tr.com.helpark.core.domain.UseCaseParams

@Parcelize
data class AddBalanceRequestDto(
    val id : Int,
    val balance : Float
) : UseCaseParams, Parcelable
