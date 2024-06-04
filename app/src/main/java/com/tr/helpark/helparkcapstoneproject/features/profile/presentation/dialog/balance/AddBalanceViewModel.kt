package com.tr.helpark.helparkcapstoneproject.features.profile.presentation.dialog.balance

import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBalanceViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): CoreViewModel(){

    fun getUserId() : String? {
        return preferencesManager.getString(PreferencesKeys.KEY_USER_ID)
    }
}