package com.tr.helpark.helparkcapstoneproject.core

import android.content.Context
import com.tr.helpark.helparkcapstoneproject.R
import com.vmlmedia.core.presentation.CoreLoadingDialog

class LoadingDialog(context: Context) : CoreLoadingDialog(context) {
    override val layoutId: Int
        get() = R.layout.dialog_loading
}