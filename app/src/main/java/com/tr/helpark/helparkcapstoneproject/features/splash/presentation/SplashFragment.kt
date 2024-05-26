package com.tr.helpark.helparkcapstoneproject.features.splash.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.helpark.helpark.common.utils.preferences.PreferencesKeys.KEY_USER_ID
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentSplashBinding
import com.vmlmedia.core.presentation.CoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<CoreViewModel, FragmentSplashBinding>(
    FragmentSplashBinding::inflate,
) {
    override val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            if (preferencesManager.getString(KEY_USER_ID).isNullOrEmpty()) {
                navigateWithAnimation(R.id.action_splashFragment_to_loginFragment)
            } else {
                navigateWithAnimation(R.id.action_splashFragment_to_homeFragment)
            }
        }
    }
}