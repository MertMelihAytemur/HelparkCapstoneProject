package com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class AddCardRequestDto(
    val userId : Int,
    val cardName : String,
    val cardAlias : String,
    val cardNumber : String,
    val cardDate : String,
    val cvv : String,
    val cardType : Int
) : UseCaseParams