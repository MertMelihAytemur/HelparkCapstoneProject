package com.tr.helpark.helparkcapstoneproject.features.login.presentation


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.formatPhoneNumber
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.setPhoneMaskWithListener
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentLoginBinding
import com.tr.helpark.helparkcapstoneproject.features.login.data.dto.request.LoginRequestDto
import com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel.LoginApiState
import com.tr.helpark.helparkcapstoneproject.features.login.domain.uimodel.LoginUiModel
import com.tr.helpark.helparkcapstoneproject.features.otp.presentation.OtpVerificationFragment.Companion.KEY_ARGUMENT_GSM_NO
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
        val phoneNumber = binding.tieNumberText.text.toString().formatPhoneNumber()

        val bundle = Bundle().apply {
            putString(KEY_ARGUMENT_GSM_NO, phoneNumber)
        }

        navigateWithAnimation(R.id.action_loginFragment_to_otpVerificationFragment,bundle)
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
                navigateWithAnimation(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }
}