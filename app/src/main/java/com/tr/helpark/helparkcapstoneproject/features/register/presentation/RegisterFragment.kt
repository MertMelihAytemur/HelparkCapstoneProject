package com.tr.helpark.helparkcapstoneproject.features.register.presentation

import androidx.fragment.app.viewModels
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>(
    FragmentRegisterBinding::inflate,
) {
    override val viewModel: RegisterViewModel by viewModels()

}