package com.tr.helpark.helparkcapstoneproject.features.home.presentation

import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksApiState
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem
import com.tr.helpark.helparkcapstoneproject.features.home.domain.usecase.GetAllParksUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllParksUseCase: GetAllParksUseCase
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


    enum class PageEvent {
        INITIAL,
        GET_ALL_PARKS_RESPONSE_RECEIVED
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val registerApiState: GetAllParksApiState = GetAllParksApiState.Initial
    )

}