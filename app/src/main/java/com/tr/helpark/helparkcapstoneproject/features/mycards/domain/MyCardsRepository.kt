package com.tr.helpark.helparkcapstoneproject.features.mycards.domain

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.AddCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.RemoveCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.AddCardUiModel
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.RemoveCardUiModel
import tr.com.helpark.core.domain.UiResult

interface MyCardsRepository {

    suspend fun addCard(addCardRequestDto: AddCardRequestDto): UiResult<AddCardUiModel, ApiErrorModel>

    suspend fun removeCard(removeCardRequestDto: RemoveCardRequestDto): UiResult<RemoveCardUiModel, ApiErrorModel>
}