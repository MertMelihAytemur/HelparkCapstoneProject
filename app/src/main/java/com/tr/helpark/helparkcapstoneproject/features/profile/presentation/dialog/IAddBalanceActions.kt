package com.tr.helpark.helparkcapstoneproject.features.profile.presentation.dialog

import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.AddBalanceRequestDto

interface IAddBalanceActions {
    fun onBalanceAdded(addBalanceRequestDto: AddBalanceRequestDto)
}