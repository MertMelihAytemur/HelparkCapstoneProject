package com.tr.helpark.helparkcapstoneproject.features.mycars.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.AddNewCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.RemoveCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.AddCarApiState
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.RemoveCarApiState
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.usecase.AddCarUseCase
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.usecase.RemoveCarUseCase
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CarPlateUiModel
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
class MyCarsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val addCarUseCase: AddCarUseCase,
    private val removeCarUseCase: RemoveCarUseCase,
    private val getProfileUseCase: GetProfileUseCase

) : CoreViewModel() {

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    private var _carList: MutableLiveData<List<CarPlateUiModel>> = MutableLiveData()

    val carList: LiveData<List<CarPlateUiModel>> get() = _carList

    init {
        getUserCars()
    }

     fun getUserCars() {
        viewModelScope.launch {
            val profile: GetProfileUiModel? = preferencesManager.getModel(
                PreferencesKeys.KEY_USER_PROFILE,
                typeOf<GetProfileUiModel>()
            )

            profile?.carPlates?.let {
                _carList.value = it
            }
        }
    }

    fun addCar(addCarRequestDto: AddNewCarRequestDto) {
        launchRequest(
            requestBody = {
                addCarUseCase(addCarRequestDto)
            },
            onSuccess = { uiModel ->
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.ADD_NEW_CAR_RESPONSE_RECEIVED,
                    addCarApiState = AddCarApiState.Success(uiModel)
                )
            }, onError = { error ->
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.ADD_NEW_CAR_RESPONSE_RECEIVED,
                    addCarApiState = AddCarApiState.Error(error)
                )
            }
        )
    }

    fun removeCar(removeCarRequestDto: RemoveCarRequestDto){
        launchRequest(
            requestBody = {
                removeCarUseCase(removeCarRequestDto)
            },
            onSuccess = { uiModel ->
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.REMOVE_CAR_RESPONSE_RECEIVED,
                    removeCarApiState = RemoveCarApiState.Success(uiModel)
                )
            },
            onError = { error ->
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.REMOVE_CAR_RESPONSE_RECEIVED,
                    removeCarApiState = RemoveCarApiState.Error(error)
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
                    pageEvent =PageEvent.GET_PROFILE_RESPONSE_RECEIVED,
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
        ADD_NEW_CAR_RESPONSE_RECEIVED,
        REMOVE_CAR_RESPONSE_RECEIVED,
        GET_PROFILE_RESPONSE_RECEIVED,
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val addCarApiState: AddCarApiState = AddCarApiState.Initial,
        val removeCarApiState: RemoveCarApiState = RemoveCarApiState.Initial,
        val getProfileApiState: GetProfileApiState = GetProfileApiState.Initial
    )
}