package com.tr.helpark.helparkcapstoneproject.features.mycards.data.remote

import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.AddCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.response.AddCardResponseDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.response.RemoveCardResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface MyCardsService {

    @POST(END_POINT_ADD_CARD)
    suspend fun addCreditCard(
        @Body addCardRequestDto: AddCardRequestDto
    ) : Response<AddCardResponseDto>

    @DELETE(END_POINT_REMOVE_CARD)
    suspend fun removeCreditCard(
        @Query("cardId") cardId: Int,
        @Query("userId") userId: Int
    ) : Response<RemoveCardResponseDto>

    private companion object {
        const val END_POINT_ADD_CARD = "/Card/AddCard"
        const val END_POINT_REMOVE_CARD = "/Card/DeleteCard"
    }
}