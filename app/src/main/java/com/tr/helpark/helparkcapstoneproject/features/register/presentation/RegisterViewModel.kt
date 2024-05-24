package com.tr.helpark.helparkcapstoneproject.features.register.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val requiredFields = booleanArrayOf(false, false, false, false)

    private val _onAllFieldFilledState = MutableLiveData(false)
    val onAllFieldFilledState: LiveData<Boolean>
        get() = _onAllFieldFilledState


    /**
     * This function is for updating fields state for enabling or disabling continue button
     */
    fun updateFieldState(position: Int, isEnabled: Boolean) {
        requiredFields[position] = isEnabled
        _onAllFieldFilledState.value = requiredFields.all { it }
    }

}