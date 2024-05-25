package com.tr.helpark.helparkcapstoneproject.features.register.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tr.helpark.helparkcapstoneproject.features.register.data.dto.request.RegisterRequestDto
import com.tr.helpark.helparkcapstoneproject.features.register.domain.uimodel.RegisterApiState
import com.tr.helpark.helparkcapstoneproject.features.register.domain.usecase.RegisterUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : CoreViewModel() {

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    private val requiredFields = booleanArrayOf(false, false, false, false)

    private val _onAllFieldFilledState = MutableLiveData(false)
    val onAllFieldFilledState: LiveData<Boolean>
        get() = _onAllFieldFilledState


    /**
     * This function is for updating fields state for enabling or disabling continue button
     */
    fun updateFieldState(position: Int, isEnabled: Boolean) {
        requiredFields[position] = isEnabled
        _onAllFieldFilledState.value = requiredFields.all { it }
    }

    fun register(registerRequestDto: RegisterRequestDto){
        launchRequest(
            requestBody = {
                registerUseCase(registerRequestDto)
            },
            onSuccess = { uiModel ->
                val apiState = RegisterApiState.Success(uiModel)
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.REGISTER_RESPONSE_RECEIVED,
                    registerApiState = apiState
                )
            },
            onError = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.REGISTER_RESPONSE_RECEIVED,
                    registerApiState = RegisterApiState.Error(it)
                )
            }
        )
    }

    enum class PageEvent{
        INITIAL,
        REGISTER_RESPONSE_RECEIVED
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val registerApiState: RegisterApiState = RegisterApiState.Initial
    )

}