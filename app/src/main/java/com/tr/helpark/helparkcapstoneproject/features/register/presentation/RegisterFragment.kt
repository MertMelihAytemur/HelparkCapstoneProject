package com.tr.helpark.helparkcapstoneproject.features.register.presentation

import android.text.SpannableString
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.addClickableLink
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

    }
    override fun initListeners() {
        with(binding){

            ivClose.setOnClickListener {
                findNavController().popBackStack()
            }

            val emailAndSmsClickListener = View.OnClickListener {

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
                    //binding.btnLogin.isEnabled = isPhoneLength
                }
            )
        }
    }

    override fun observeEvents() {

    }
}