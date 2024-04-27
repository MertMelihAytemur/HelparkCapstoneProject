package com.tr.helpark.helparkcapstoneproject.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClientPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

}