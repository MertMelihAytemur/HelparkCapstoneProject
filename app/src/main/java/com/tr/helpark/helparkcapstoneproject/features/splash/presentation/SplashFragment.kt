package com.tr.helpark.helparkcapstoneproject.features.splash.presentation

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment<FragmentSplashBinding,SplashViewModel>(
    FragmentSplashBinding::inflate,
) {
    override val viewModel: SplashViewModel by viewModels()


    override fun onViewReady() {
       Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }, 3000)
    }

    override fun initListeners() {

    }

    override fun observeEvents() {

    }
}