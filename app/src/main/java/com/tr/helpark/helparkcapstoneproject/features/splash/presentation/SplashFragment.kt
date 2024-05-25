package com.tr.helpark.helparkcapstoneproject.features.splash.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentSplashBinding
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<CoreViewModel,FragmentSplashBinding>(
    FragmentSplashBinding::inflate,
) {
    override val viewModel: SplashViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            navigateWithAnimation(action)
        }
    }
}