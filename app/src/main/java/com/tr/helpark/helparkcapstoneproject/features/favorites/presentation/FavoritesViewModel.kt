package com.tr.helpark.helparkcapstoneproject.features.favorites.presentation

import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.features.favorites.data.dto.request.GetFavoritesRequestDto
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesApiState
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.usecase.GetFavoritesUseCase
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.request.ToggleFavoriteParkRequestDto
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.ToggleFavoriteApiState
import com.tr.helpark.helparkcapstoneproject.features.home.domain.usecase.ToggleFavoriteUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val preferencesManager: PreferencesManager
) : CoreViewModel() {

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    fun getFavorites(getFavoritesRequestDto: GetFavoritesRequestDto) {
        launchRequest(
            requestBody = {
                getFavoritesUseCase(getFavoritesRequestDto)
            }, onSuccess = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.GET_FAVORITES_RESPONSE_RECEIVED,
                    getFavoritesApiState = GetFavoritesApiState.Success(it)
                )
            }, onError = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.GET_FAVORITES_RESPONSE_RECEIVED,
                    getFavoritesApiState = GetFavoritesApiState.Error(it)
                )
            }
        )
    }

    fun toggleFavorite(toggleFavoriteParkRequestDto: ToggleFavoriteParkRequestDto){
        launchRequest(
            requestBody = {
                toggleFavoriteUseCase(toggleFavoriteParkRequestDto)
            },
            onSuccess = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.TOGGLE_FAVORITE_RESPONSE_RECEIVED,
                    toggleFavoriteApiState = ToggleFavoriteApiState.Success(it)
                )
            },
            onError = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.TOGGLE_FAVORITE_RESPONSE_RECEIVED,
                    toggleFavoriteApiState = ToggleFavoriteApiState.Error(it)
                )
            }
        )
    }

    enum class PageEvent {
        INITIAL,
        GET_FAVORITES_RESPONSE_RECEIVED,
        TOGGLE_FAVORITE_RESPONSE_RECEIVED
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val getFavoritesApiState: GetFavoritesApiState = GetFavoritesApiState.Initial,
        val toggleFavoriteApiState: ToggleFavoriteApiState = ToggleFavoriteApiState.Initial
    )
}