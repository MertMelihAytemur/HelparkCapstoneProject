package com.tr.helpark.helparkcapstoneproject.features.splash.presentation

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding,SplashViewModel>(
    FragmentSplashBinding::inflate,
) {
    override val viewModel: SplashViewModel by viewModels()


    override fun onViewReady() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            navigateWithAnimation(action)
        }
    }

    override fun initListeners() {

    }

    override fun observeEvents() {

    }
}