package com.tr.helpark.helparkcapstoneproject.features.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.AddBalanceRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.DeleteUserAccountRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.AddBalanceApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CardUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.DeleteUserAccountApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase.AddBalanceUseCase
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase.DeleteUserAccountUseCase
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
class ProfileViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val addBalanceUseCase: AddBalanceUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase
) : CoreViewModel() {

    private var _cardList: MutableLiveData<List<CardUiModel>> = MutableLiveData()
    val cardList: LiveData<List<CardUiModel>> get() = _cardList

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

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

    fun addBalance(addBalanceRequestDto: AddBalanceRequestDto) {
        launchRequest(
            requestBody = {
                addBalanceUseCase(addBalanceRequestDto)
            },
            onSuccess = { uiModel ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.ADD_BALANCE_RESPONSE_RECEIVED,
                        addBalanceApiState = AddBalanceApiState.Success(uiModel)
                    )
                }
            },
            onError = { error ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.ADD_BALANCE_RESPONSE_RECEIVED,
                        addBalanceApiState = AddBalanceApiState.Error(error)
                    )
                }
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

    fun deleteUserAccount(deleteUserAccountRequestDto: DeleteUserAccountRequestDto) {
        launchRequest(
            requestBody = {
                deleteUserAccountUseCase(deleteUserAccountRequestDto)
            },
            onSuccess = { uiModel ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.DELETE_USER_ACCOUNT_RESPONSE_RECEIVED,
                        deleteUserAccountApiState = DeleteUserAccountApiState.Success(uiModel)
                    )
                }
            },
            onError = { error ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.DELETE_USER_ACCOUNT_RESPONSE_RECEIVED,
                        deleteUserAccountApiState = DeleteUserAccountApiState.Error(error)
                    )
                }
            }
        )
    }

    enum class PageEvent {
        INITIAL,
        ADD_BALANCE_RESPONSE_RECEIVED,
        GET_PROFILE_RESPONSE_RECEIVED,
        DELETE_USER_ACCOUNT_RESPONSE_RECEIVED
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val addBalanceApiState: AddBalanceApiState = AddBalanceApiState.Initial,
        val getProfileApiState: GetProfileApiState = GetProfileApiState.Initial,
        val deleteUserAccountApiState: DeleteUserAccountApiState = DeleteUserAccountApiState.Initial
    )
}