package com.tr.helpark.helparkcapstoneproject.features.mycards.data

import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.AddCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.RemoveCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.response.toDomain
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.remote.MyCardsService
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.MyCardsRepository
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.AddCardUiModel
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.RemoveCardUiModel
import tr.com.helpark.core.data.ApiExecutor
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.domain.UiResult
import tr.com.helpark.core.domain.parseError
import javax.inject.Inject

class MyCardsRepositoryImpl @Inject constructor(
    private val myCardsService: MyCardsService
) : MyCardsRepository, ApiExecutor {
    override suspend fun addCard(addCardRequestDto: AddCardRequestDto): UiResult<AddCardUiModel, ApiErrorModel> {
        val apiResult = execute {
            myCardsService.addCreditCard(addCardRequestDto)
        }

        return when (apiResult) {
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }

    override suspend fun removeCard(removeCardRequestDto: RemoveCardRequestDto): UiResult<RemoveCardUiModel, ApiErrorModel> {
        val apiResult = execute {
            myCardsService.removeCreditCard(
                removeCardRequestDto.cardId,
                removeCardRequestDto.userId
            )
        }

        return when (apiResult) {
            is ApiResult.Success -> {
                UiResult.Success(apiResult.response?.toDomain())
            }

            is ApiResult.Error -> {
                UiResult.Error(parseError<ApiErrorModel>(apiResult).error)
            }
        }
    }

}