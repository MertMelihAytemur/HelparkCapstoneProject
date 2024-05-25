package com.tr.helpark.helparkcapstoneproject.features.register.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class RegisterRequestDto(
    val name : String,
    val surname : String,
    val email : String,
    val phoneNumber : String
    //val isEmailNotificationPermitted : Boolean,
   // val isSmsNotificationPermitted : Boolean,
) : UseCaseParams