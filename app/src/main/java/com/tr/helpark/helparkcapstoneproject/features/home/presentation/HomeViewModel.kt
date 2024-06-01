package com.tr.helpark.helparkcapstoneproject.features.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.request.ToggleFavoriteParkRequestDto
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksApiState
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.ToggleFavoriteApiState
import com.tr.helpark.helparkcapstoneproject.features.home.domain.usecase.GetAllParksUseCase
import com.tr.helpark.helparkcapstoneproject.features.home.domain.usecase.ToggleFavoriteUseCase
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.FavouriteUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase.GetProfileUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllParksUseCase: GetAllParksUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val preferencesManager: PreferencesManager
) : CoreViewModel() {

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    var lastClickedMarkerId: Int = -1

    var parkList: List<GetAllParksUiModelItem> = mutableListOf()

    private var _favoriteParkList: MutableLiveData<List<FavouriteUiModel>> = MutableLiveData()
    val favoriteParkList: LiveData<List<FavouriteUiModel>>
        get() = _favoriteParkList

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

    fun getProfile(userId: String) {
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
            },showLoading = false
        )
    }

    fun navigateToNextScreen() {
        _pageStateFlow.value = _pageStateFlow.value.copy(
            pageEvent = PageEvent.NAVIGATE_TO_NEXT_SCREEN
        )
    }

    fun getFavoriteParks() {
        viewModelScope.launch {
            val profile: GetProfileUiModel? = preferencesManager.getModel(
                PreferencesKeys.KEY_USER_PROFILE,
                typeOf<GetProfileUiModel>()
            )

            profile?.favourite?.let {
                _favoriteParkList.value = it
            }
        }
    }

    enum class PageEvent {
        INITIAL,
        GET_ALL_PARKS_RESPONSE_RECEIVED,
        GET_PROFILE_RESPONSE_RECEIVED,
        TOGGLE_FAVORITE_RESPONSE_RECEIVED,
        NAVIGATE_TO_NEXT_SCREEN
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val registerApiState: GetAllParksApiState = GetAllParksApiState.Initial,
        val getProfileApiState: GetProfileApiState = GetProfileApiState.Initial,
        val toggleFavoriteApiState: ToggleFavoriteApiState = ToggleFavoriteApiState.Initial
    )

}