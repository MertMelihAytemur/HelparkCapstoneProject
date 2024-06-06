package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.presentation

import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.GetProfileRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.usecase.GetProfileUseCase
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.CancelReservationRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.CancelReservationApiState
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.usecase.CancelReservationUseCase
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.dto.request.GetReservationHistoryRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.GetReservationHistoryApiState
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.usecase.GetReservationHistoryUseCase
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ReservationHistoryViewModel @Inject constructor(
    private val getReservationHistoryUseCase: GetReservationHistoryUseCase,
    private val cancelReservationUseCase: CancelReservationUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : CoreViewModel() {

    private var _pageStateFlow =
        MutableStateFlow(PageState())
    val pageStateFlow: StateFlow<PageState> = _pageStateFlow.asStateFlow()

    fun getReservationHistory(userId: Int) {
        launchRequest(
            requestBody = {
                getReservationHistoryUseCase(GetReservationHistoryRequestDto(userId))
            },
            onSuccess = { uiModel ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.ON_RESERVATION_HISTORY_RESPONSE_RECEIVED,
                        reservationHistoryApiState = GetReservationHistoryApiState.Success(uiModel)
                    )
                }
            },
            onError = { error ->
                _pageStateFlow.update {
                    it.copy(
                        pageEvent = PageEvent.ON_RESERVATION_HISTORY_RESPONSE_RECEIVED,
                        reservationHistoryApiState = GetReservationHistoryApiState.Error(error)
                    )
                }
            }
        )
    }

    fun cancelReservation(resId: Int) {
        launchRequest(
            requestBody = {
                cancelReservationUseCase(CancelReservationRequestDto(resId))
            },
            onSuccess = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.CANCEL_RESERVATION_RESPONSE_RECEIVED,
                    cancelReservationApiState = CancelReservationApiState.Success(it)
                )
            },
            onError = {
                _pageStateFlow.value = _pageStateFlow.value.copy(
                    pageEvent = PageEvent.CANCEL_RESERVATION_RESPONSE_RECEIVED,
                    cancelReservationApiState = CancelReservationApiState.Error(it)
                )
            }
        )
    }

    fun getProfile(userId: String) {
        launchRequest(
            requestBody = {
                getProfileUseCase(GetProfileRequestDto(userId))
            },
            showLoading = false
        )
    }

    enum class PageEvent {
        INITIAL,
        ON_RESERVATION_HISTORY_RESPONSE_RECEIVED,
        CANCEL_RESERVATION_RESPONSE_RECEIVED
    }

    data class PageState(
        val pageEvent: PageEvent = PageEvent.INITIAL,
        val reservationHistoryApiState: GetReservationHistoryApiState = GetReservationHistoryApiState.Initial,
        val cancelReservationApiState: CancelReservationApiState = CancelReservationApiState.Initial
    )
}