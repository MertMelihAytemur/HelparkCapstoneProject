package com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel

import android.os.Parcelable
import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import kotlinx.parcelize.Parcelize
import tr.com.helpark.core.domain.UiError
import tr.com.helpark.core.domain.UiModel

@Parcelize
data class GetProfileUiModel(
    val active: Int?,
    val balance: Int?,
    val carPlates: List<CarPlateUiModel>?,
    val card: List<CardUiModel>?,
    val debt: Int?,
    val email: String?,
    val id: Int?,
    val name: String?,
    val phoneNumber: String?,
    val surname: String?
) : UiModel, Parcelable

@Parcelize
data class CarPlateUiModel(
    val active: Int?,
    val description: String?,
    val fuelTypeId: Int?,
    val id: Int?,
    val model: String?,
    val plate: String?,
    val userId: Int?
) : Parcelable

@Parcelize
data class CardUiModel(
    val active: String?,
    val cardAlias: String?,
    val cardDate: String?,
    val cardName: String?,
    val cardNumber: String?,
    val cardTypeId: Int?,
    val cvv: String?,
    val description: String?,
    val id: Int?,
    val userId: Int?
) : Parcelable

sealed interface GetProfileApiState {
    object Initial : GetProfileApiState
    data class Success(val uiModel: GetProfileUiModel?) : GetProfileApiState
    data class Error(val error: UiError<ApiErrorModel>) : GetProfileApiState
}
