package com.tr.helpark.helparkcapstoneproject.features.login.presentation


import androidx.fragment.app.viewModels
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment<FragmentLoginBinding,LoginViewModel>(
    FragmentLoginBinding::inflate
) {
    override val viewModel: LoginViewModel by viewModels()



}