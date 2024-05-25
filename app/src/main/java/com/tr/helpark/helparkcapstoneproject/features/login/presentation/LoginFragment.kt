package com.tr.helpark.helparkcapstoneproject.features.login.presentation


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tr.helpark.helparkcapstoneproject.common.extensions.formatPhoneNumber
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.setPhoneMaskWithListener
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentLoginBinding
import com.tr.helpark.helparkcapstoneproject.features.login.data.dto.request.LoginRequestDto
import com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel.LoginApiState
import com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel.LoginUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {
    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tieNumberText.setPhoneMaskWithListener(
            onPhoneCompleted = { isPhoneLength ->
                if (isPhoneLength) hideKeyboard()
                binding.btnLogin.isEnabled = isPhoneLength
            }
        )

        initListeners()

        collectPageState(viewModel.pageStateFlow) { state ->
            when (state.pageEvent) {
                LoginViewModel.PageEvent.INITIAL -> {}

                LoginViewModel.PageEvent.LOGIN_RESPONSE_RECEIVED -> {
                    onLoginResponseReceived(state.registerApiState)
                }
            }
        }
    }

    private fun onLoginResponseReceived(apiState: LoginApiState) {
        when (apiState) {
            is LoginApiState.Initial -> {}

            is LoginApiState.Success -> {
                onLoginSuccess(apiState.uiModel)
            }

            is LoginApiState.Error -> {
                handleNetworkError(apiState.error)
            }
        }
    }

    private fun onLoginSuccess(uiModel: LoginUiModel?) {
        showToastMessage(
            uiModel?.message ?: "Login success",
            toastType = ToastMessageType.GENERAL_SUCCESS
        )

        val phoneNumber = binding.tieNumberText.text.toString().formatPhoneNumber()

        val action = LoginFragmentDirections.actionLoginFragmentToOtpVerificationFragment(
            phoneNumber
        )
        navigateWithAnimation(action)

    }

    private fun initListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                val phoneNumber = tieNumberText.text.toString().formatPhoneNumber()

                viewModel.login(
                    LoginRequestDto(
                        phoneNumber = phoneNumber
                    )
                )
            }

            btnRegister.setOnClickListener {
                navigateWithAnimation(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
    }
}