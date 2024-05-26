package com.tr.helpark.helparkcapstoneproject.features.home.presentation

import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksApiState
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem
import com.tr.helpark.helparkcapstoneproject.features.home.domain.usecase.GetAllParksUseCase
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase.GetProfileUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllParksUseCase: GetAllParksUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : CoreViewModel() {

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    var lastClickedMarkerId: Int = -1

    var parkList : List<GetAllParksUiModelItem> = mutableListOf()
    fun getAllParks() {
        launchRequest(
            requestBody = {
                getAllParksUseCase()
            },
            onSuccess = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.GET_ALL_PARKS_RESPONSE_RECEIVED,
                    registerApiState = GetAllParksApiState.Success(it)
                )
            },
            onError = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.GET_ALL_PARKS_RESPONSE_RECEIVED,
                    registerApiState = GetAllParksApiState.Error(it)
                )
            }
        )
    }

    fun getProfile(userId : String){
        launchRequest(
            requestBody = {
                getProfileUseCase(GetProfileRequestDto(userId))
            },
            onSuccess = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.GET_PROFILE_RESPONSE_RECEIVED,
                    getProfileApiState = GetProfileApiState.Success(it)
                )
            },
            onError = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.GET_PROFILE_RESPONSE_RECEIVED,
                    getProfileApiState = GetProfileApiState.Error(it)
                )
            },
            showLoading = false
        )
    }

    fun navigateToNextScreen(){
        _pageStateFlow.value = _pageStateFlow.value.copy(
            pageEvent = PageEvent.NAVIGATE_TO_NEXT_SCREEN
        )
    }


    enum class PageEvent {
        INITIAL,
        GET_ALL_PARKS_RESPONSE_RECEIVED,
        GET_PROFILE_RESPONSE_RECEIVED,
        NAVIGATE_TO_NEXT_SCREEN
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val registerApiState: GetAllParksApiState = GetAllParksApiState.Initial,
        val getProfileApiState: GetProfileApiState = GetProfileApiState.Initial
    )

}