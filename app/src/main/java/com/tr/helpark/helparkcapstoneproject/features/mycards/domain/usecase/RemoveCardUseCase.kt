package com.tr.helpark.helparkcapstoneproject.features.mycards.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.RemoveCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.MyCardsRepository
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.RemoveCardUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class RemoveCardUseCase @Inject constructor(
    private val myCardsRepository: MyCardsRepository
)  : UseCase<RemoveCardUiModel, ApiErrorModel>{
    override suspend fun invoke(params: UseCaseParams?): UiResult<RemoveCardUiModel, ApiErrorModel> {
        return myCardsRepository.removeCard(params as RemoveCardRequestDto)
    }
}