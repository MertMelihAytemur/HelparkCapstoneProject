package com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.response


import com.google.gson.annotations.SerializedName
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CarPlateUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CardUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel

data class GetProfileResponseDto(
    @SerializedName("active")
    val active: Int?,
    @SerializedName("balance")
    val balance: Int?,
    @SerializedName("carPlates")
    val carPlates: List<CarPlate?>?,
    @SerializedName("card")
    val card: List<Card?>?,
    @SerializedName("debt")
    val debt: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("surname")
    val surname: String?
)

data class CarPlate(
    @SerializedName("active")
    val active: Int?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("fuelTypeId")
    val fuelTypeId: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("plate")
    val plate: String?,
    @SerializedName("userId")
    val userId: Int?
)

data class Card(
    @SerializedName("active")
    val active: String?,
    @SerializedName("cardAlias")
    val cardAlias: String?,
    @SerializedName("cardDate")
    val cardDate: String?,
    @SerializedName("cardName")
    val cardName: String?,
    @SerializedName("cardNumber")
    val cardNumber: String?,
    @SerializedName("cardTypeId")
    val cardTypeId: Int?,
    @SerializedName("cvv")
    val cvv: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("userId")
    val userId: Int?
)

// Extension function for GetProfileResponseDto
fun GetProfileResponseDto.toDomain(): GetProfileUiModel {
    return GetProfileUiModel(
        active = this.active,
        balance = this.balance,
        carPlates = this.carPlates?.map { it?.toDomain() },
        card = this.card?.map { it?.toDomain() },
        debt = this.debt,
        email = this.email,
        id = this.id,
        name = this.name,
        phoneNumber = this.phoneNumber,
        surname = this.surname
    )
}

// Extension function for CarPlate
fun CarPlate.toDomain(): CarPlateUiModel {
    return CarPlateUiModel(
        active = this.active,
        description = this.description,
        fuelTypeId = this.fuelTypeId,
        id = this.id,
        model = this.model,
        plate = this.plate,
        userId = this.userId
    )
}

// Extension function for Card
fun Card.toDomain(): CardUiModel {
    return CardUiModel(
        active = this.active,
        cardAlias = this.cardAlias,
        cardDate = this.cardDate,
        cardName = this.cardName,
        cardNumber = this.cardNumber,
        cardTypeId = this.cardTypeId,
        cvv = this.cvv,
        description = this.description,
        id = this.id,
        userId = this.userId
    )
}