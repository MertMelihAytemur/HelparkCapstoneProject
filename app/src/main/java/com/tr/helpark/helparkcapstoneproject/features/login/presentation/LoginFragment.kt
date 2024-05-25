package com.tr.helpark.helparkcapstoneproject.features.login.presentation


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tr.helpark.helparkcapstoneproject.common.extensions.formatPhoneNumber
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.setPhoneMaskWithListener
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentLoginBinding
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
    }

    private fun initListeners() {
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

}