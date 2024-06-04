package com.tr.helpark.helparkcapstoneproject.features.parkdetail.data.dto.request

import tr.com.helpark.core.domain.UseCaseParams

data class GetParkDetailRequestDto(
    val parkId : String
) : UseCaseParams
