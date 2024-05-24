package com.tr.helpark.helparkcapstoneproject.common.extensions

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.postValueIfDifferent(newValue: T) {
    if (this.value != newValue)
        this@postValueIfDifferent.postValue(newValue)
}