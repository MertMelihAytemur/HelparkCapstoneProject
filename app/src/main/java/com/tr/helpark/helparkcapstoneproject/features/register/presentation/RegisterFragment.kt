package com.tr.helpark.helparkcapstoneproject.features.register.presentation

import android.text.SpannableString
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.addClickableLink
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.setPhoneMaskWithListener
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>(
    FragmentRegisterBinding::inflate,
) {
    override val viewModel: RegisterViewModel by viewModels()

    private var emailPermission = false
    private var smsPermission = false
    override fun onViewReady() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.gray_soft_f8)
        setRegisterButtonStatus()
        initUi()
    }

    override fun initListeners() {
        with(binding) {

            ivClose.setOnClickListener {
                findNavController().popBackStack()
            }

            val emailAndSmsClickListener = View.OnClickListener {
                navigateWithAnimation(
                    RegisterFragmentDirections.actionRegisterFragmentToSmsEmailWebViewFragment()
                )
            }

            tvEmailPermission.addClickableLink(
                getString(R.string.user_permission_mail),
                SpannableString(getString(R.string.spannable_text_here)),
                R.color.text_color_black
            ) {
                tvEmailPermission.setOnClickListener(emailAndSmsClickListener)
            }

            tvSmsPermission.addClickableLink(
                getString(R.string.user_permission_sms),
                SpannableString(getString(R.string.spannable_text_here)),
                R.color.text_color_black
            ) {
                tvSmsPermission.setOnClickListener(emailAndSmsClickListener)
            }

            ivCheckBoxSmsPermission.setOnClickListener {
                smsPermission = !smsPermission

                if (smsPermission) {
                    ivCheckBoxSmsPermission.setImageResource(R.drawable.rectangular_checkbox_checked)
                } else {
                    ivCheckBoxSmsPermission.setImageResource(R.drawable.rectangular_checkbox_unchecked)
                }
            }

            ivCheckBoxEmailPermission.setOnClickListener {
                emailPermission = !emailPermission

                if (emailPermission) {
                    ivCheckBoxEmailPermission.setImageResource(R.drawable.rectangular_checkbox_checked)
                } else {
                    ivCheckBoxEmailPermission.setImageResource(R.drawable.rectangular_checkbox_unchecked)
                }
            }

            binding.tiePhone.setPhoneMaskWithListener(
                onPhoneCompleted = { isPhoneLength ->
                    if (isPhoneLength) hideKeyboard()
                    viewModel.updateFieldState(3, isPhoneLength)
                }
            )
        }
    }

    private fun initUi(){
        if(smsPermission){
            binding.ivCheckBoxSmsPermission.setImageResource(R.drawable.rectangular_checkbox_checked)
        }

        if(emailPermission){
            binding.ivCheckBoxEmailPermission.setImageResource(R.drawable.rectangular_checkbox_checked)
        }
    }

    private fun setRegisterButtonStatus() {
        with(binding) {
            tieFirstName.doAfterTextChanged {
                viewModel.updateFieldState(0, it?.isNotEmpty() ?: false)
            }

            tieSurname.doAfterTextChanged {
                viewModel.updateFieldState(1, it?.isNotEmpty() ?: false)
            }

            tieEmail.doAfterTextChanged {
                viewModel.updateFieldState(2, it?.isNotEmpty() ?: false)
            }
        }
    }

    override fun observeEvents() {
        viewModel.onAllFieldFilledState.observe(viewLifecycleOwner) { isAllFieldFilled ->
            binding.btnRegister.isEnabled = isAllFieldFilled
        }
    }
}