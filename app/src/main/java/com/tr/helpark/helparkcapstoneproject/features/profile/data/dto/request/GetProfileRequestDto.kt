package com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class GetProfileRequestDto(
    val userId : String
) : UseCaseParams
