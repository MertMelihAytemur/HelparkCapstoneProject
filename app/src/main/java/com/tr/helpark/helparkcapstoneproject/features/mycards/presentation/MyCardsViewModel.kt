package com.tr.helpark.helparkcapstoneproject.features.mycards.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.AddCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.RemoveCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.AddCardApiState
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.RemoveCardApiState
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.usecase.AddCardUseCase
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.usecase.RemoveCardUseCase
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CardUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase.GetProfileUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class MyCardsViewModel @Inject constructor(
    private val addCardUseCase: AddCardUseCase,
    private val removeCardUseCase: RemoveCardUseCase,
    private val preferencesManager: PreferencesManager,
    private val getProfileUseCase: GetProfileUseCase
) : CoreViewModel() {

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    private var _cardList: MutableLiveData<List<CardUiModel>> = MutableLiveData()

    val cardList: LiveData<List<CardUiModel>> get() = _cardList

    fun getUserCards() {
        viewModelScope.launch {
            val profile: GetProfileUiModel? = preferencesManager.getModel(
                PreferencesKeys.KEY_USER_PROFILE,
                typeOf<GetProfileUiModel>()
            )

            profile?.card?.let {
                _cardList.value = it
            }
        }
    }

    fun addCard(addCardRequestDto: AddCardRequestDto) {
        launchRequest(
            requestBody = { addCardUseCase(addCardRequestDto) },
            onSuccess = { uiModel ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.ADD_CARD_RESPONSE_RECEIVED,
                        addCardApiState = AddCardApiState.Success(uiModel)
                    )
                }
            },
            onError = { error ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.ADD_CARD_RESPONSE_RECEIVED,
                        addCardApiState = AddCardApiState.Error(error)
                    )
                }
            }
        )
    }

    fun removeCard(removeCardRequestDto: RemoveCardRequestDto) {
        launchRequest(
            requestBody = { removeCardUseCase(removeCardRequestDto) },

            onSuccess = { uiModel ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.REMOVE_CARD_RESPONSE_RECEIVED,
                        removeCardApiState = RemoveCardApiState.Success(uiModel)
                    )
                }
            },

            onError = { error ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.REMOVE_CARD_RESPONSE_RECEIVED,
                        removeCardApiState = RemoveCardApiState.Error(error)
                    )
                }
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

    enum class PageEvent {
        INITIAL,
        ADD_CARD_RESPONSE_RECEIVED,
        REMOVE_CARD_RESPONSE_RECEIVED,
        GET_PROFILE_RESPONSE_RECEIVED
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val addCardApiState: AddCardApiState = AddCardApiState.Initial,
        val removeCardApiState: RemoveCardApiState = RemoveCardApiState.Initial,
        val getProfileApiState: GetProfileApiState = GetProfileApiState.Initial
    )
}