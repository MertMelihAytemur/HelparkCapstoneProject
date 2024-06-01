package com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.response

import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.AddBalanceUiModel

data class AddBalanceDto(
    val message: String?
)

fun AddBalanceDto.toDomain(): AddBalanceUiModel {
    return AddBalanceUiModel(
        message = this.message
    )
}

