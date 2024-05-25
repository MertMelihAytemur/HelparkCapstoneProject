package com.tr.helpark.helparkcapstoneproject.core.base


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.viewbinding.ViewBinding
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType
import com.tr.helpark.helparkcapstoneproject.core.model.ApiErrorModel
import com.tr.helpark.helparkcapstoneproject.features.main.MapsActivity
import com.vmlmedia.core.presentation.CoreFragment
import com.vmlmedia.core.presentation.CoreViewModel
import com.vmlmedia.core.presentation.LoadingInterface
import tr.com.helpark.core.domain.UiError

/**
 *Created by Mert Melih Aytemur on 1/19/2024.
 */

typealias Inflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VM : CoreViewModel, VB : ViewBinding>(
    private val inflater: Inflater<VB>,
) : CoreFragment<VM>() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override val loadingInterface: LoadingInterface
        get() = (requireActivity() as MapsActivity).loadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = this.inflater.invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun handleNetworkError(
        error: UiError<ApiErrorModel>,
    ) {
        when (error) {
            is UiError.Authentication -> {
                showToastMessage(
                    error.errorBody?.message ?: "Authentication error",
                    toastType = ToastMessageType.GENERAL_ERROR
                )
            }

            is UiError.NoInternet -> {
                showToastMessage(
                    "No internet connection",
                    toastType = ToastMessageType.GENERAL_ERROR
                )
            }

            is UiError.Server -> {
                showToastMessage(
                    error.errorBody?.message ?: "Server error",
                    toastType = ToastMessageType.GENERAL_ERROR
                )
            }

            is UiError.IO -> {
                showToastMessage(
                    error.message ?: "IO error",
                    toastType = ToastMessageType.GENERAL_ERROR
                )
            }
        }
    }

    open fun hideKeyboard(){
        val inputManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}