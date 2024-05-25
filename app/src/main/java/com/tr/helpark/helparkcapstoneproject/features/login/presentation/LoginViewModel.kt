package com.tr.helpark.helparkcapstoneproject.features.login.presentation

import com.tr.helpark.helparkcapstoneproject.features.login.data.dto.request.LoginRequestDto
import com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel.LoginApiState
import com.tr.helpark.helparkcapstoneproject.features.login.domain.usecase.LoginUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 *Created by Mert Melih Aytemur on 1/23/2024.
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : CoreViewModel() {

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    fun login(loginRequestDto: LoginRequestDto){
        launchRequest(
            requestBody = {
                loginUseCase(loginRequestDto)
            },
            onSuccess = {uiModel ->
                val apiState = LoginApiState.Success(uiModel)
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.LOGIN_RESPONSE_RECEIVED,
                    registerApiState = apiState
                )
            },
            onError = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.LOGIN_RESPONSE_RECEIVED,
                    registerApiState = LoginApiState.Error(it)
                )
            }
        )
    }
    enum class PageEvent{
        INITIAL,
        LOGIN_RESPONSE_RECEIVED
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val registerApiState: LoginApiState = LoginApiState.Initial
    )
}