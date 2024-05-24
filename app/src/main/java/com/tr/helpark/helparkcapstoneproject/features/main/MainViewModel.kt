package com.tr.helpark.helparkcapstoneproject.features.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    var isLocationServicesEnabled: MutableLiveData<Boolean> = MutableLiveData(false)

    var isLocationPermissionGranted: MutableLiveData<Boolean> = MutableLiveData(false)

    // todo değişken ismini düzenle, bu ismin bir anlamı yok neyi combine ediyoruz?
    val combinedLiveData = MediatorLiveData<Boolean>()

    private val _networkStateLiveData = MutableSharedFlow<Boolean>()
    val networkStateLiveData: SharedFlow<Boolean> = _networkStateLiveData

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
}