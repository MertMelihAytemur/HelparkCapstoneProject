package com.tr.helpark.helparkcapstoneproject.features.login.presentation


import androidx.fragment.app.viewModels
import com.tr.helpark.helparkcapstoneproject.common.extensions.formatPhoneNumber
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.setPhoneMaskWithListener
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(
    FragmentLoginBinding::inflate
) {
    override val viewModel: LoginViewModel by viewModels()


    override fun onViewReady() {
        binding.tieNumberText.setPhoneMaskWithListener(
            onPhoneCompleted = { isPhoneLength ->
                if (isPhoneLength) hideKeyboard()
                binding.btnLogin.isEnabled = isPhoneLength
            }
        )
    }

    override fun initListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                val phoneNumber = tieNumberText.text.toString().formatPhoneNumber()

                val action = LoginFragmentDirections.actionLoginFragmentToOtpVerificationFragment(
                    phoneNumber
                )
                navigateWithAnimation(action)
            }

            btnRegister.setOnClickListener {
                navigateWithAnimation(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
    }

    override fun observeEvents() {
    }

}