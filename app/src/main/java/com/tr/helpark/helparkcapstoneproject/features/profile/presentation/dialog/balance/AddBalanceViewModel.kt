package com.tr.helpark.helparkcapstoneproject.features.profile.presentation.dialog.balance

import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.AddBalanceRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.AddBalanceApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase.AddBalanceUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tr.com.helpark.core.util.logD
import javax.inject.Inject

@HiltViewModel
class AddBalanceViewModel @Inject constructor(
    private val addBalanceUseCase: AddBalanceUseCase,
    private val preferencesManager: PreferencesManager
): CoreViewModel(){

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    fun addBalance(addBalanceRequestDto: AddBalanceRequestDto){
        launchRequest(
            requestBody = {
                logD("addBalanceUseCase 1")
                addBalanceUseCase(addBalanceRequestDto)
            },
            onSuccess = {uiModel ->
                logD("addBalanceUseCase 2")
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.ADD_BALANCE_RESPONSE_RECEIVED,
                        addBalanceApiState = AddBalanceApiState.Success(uiModel)
                    )
                }
            },
            onError = {error ->
                logD("addBalanceUseCase 3")
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.ADD_BALANCE_RESPONSE_RECEIVED,
                        addBalanceApiState = AddBalanceApiState.Error(error)
                    )
                }
            }
        )
    }

    fun getUserId() : String? {
        return preferencesManager.getString(PreferencesKeys.KEY_USER_ID)
    }

    enum class PageEvent{
        INITIAL,
        ADD_BALANCE_RESPONSE_RECEIVED
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val addBalanceApiState : AddBalanceApiState = AddBalanceApiState.Initial
    )
}