package com.tr.helpark.helparkcapstoneproject.features.mycards.domain.usecase

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.AddCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.MyCardsRepository
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.AddCardUiModel
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.UseCase
import tr.com.helpark.core.domain.UseCaseParams
import javax.inject.Inject

class AddCardUseCase @Inject constructor(
    private val myCardsRepository: MyCardsRepository
)  : UseCase<AddCardUiModel, ApiErrorModel> {
    override suspend fun invoke(params: UseCaseParams?): UiResult<AddCardUiModel, ApiErrorModel> {
        return myCardsRepository.addCard(params as AddCardRequestDto)
    }

}