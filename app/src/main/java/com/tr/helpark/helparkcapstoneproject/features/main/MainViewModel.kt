package com.tr.helpark.helparkcapstoneproject.features.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.helpark.helpark.common.utils.preferences.PreferencesKeys.KEY_USER_RESERVATION_ID
import com.tr.helpark.helparkcapstoneproject.common.helper.FirebaseHelper
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model.ReservationStatusType
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val firebaseHelper: FirebaseHelper
) : CoreViewModel() {

    var isLocationServicesEnabled: MutableLiveData<Boolean> = MutableLiveData(false)

    var isLocationPermissionGranted: MutableLiveData<Boolean> = MutableLiveData(false)

    // todo değişken ismini düzenle, bu ismin bir anlamı yok neyi combine ediyoruz?
    val combinedLiveData = MediatorLiveData<Boolean>()

    private val _networkStateLiveData = MutableSharedFlow<Boolean>()
    val networkStateLiveData: SharedFlow<Boolean> = _networkStateLiveData

    var userLocation: LatLng? = null

    init {
        // Add the LiveData sources to the MediatorLiveData
        combinedLiveData.addSource(isLocationServicesEnabled) { isLocationEnabled ->
            val isPermissionGranted = isLocationPermissionGranted.value ?: false
            combinedLiveData.value = isLocationEnabled && isPermissionGranted
        }

        combinedLiveData.addSource(isLocationPermissionGranted) { isPermissionGranted ->
            val isLocationEnabled = isLocationServicesEnabled.value ?: false
            combinedLiveData.value = isLocationEnabled && isPermissionGranted
        }
    }

    fun updateNetworkState(networkState: Boolean) {
        viewModelScope.launch {
            _networkStateLiveData.emit(networkState)
        }
    }

    fun createReservation() {
        preferencesManager.getString(KEY_USER_RESERVATION_ID)?.let { userId ->
            firebaseHelper.addOrUpdateReservationStatus(userId, ReservationStatusType.PENDING.value)
        }
    }

    fun cancelReservation() {
        preferencesManager.getString(KEY_USER_RESERVATION_ID)?.let { userId ->
            firebaseHelper.addOrUpdateReservationStatus(userId, ReservationStatusType.CANCELLED.value)
        }
    }

    fun removeReservationReference(){
        preferencesManager.getString(KEY_USER_RESERVATION_ID)?.let { userId ->
            firebaseHelper.deleteReservationStatus(userId)
        }
    }
}