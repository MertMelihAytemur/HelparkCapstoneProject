package com.tr.helpark.helparkcapstoneproject.features.home.presentation

import androidx.fragment.app.viewModels
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    FragmentHomeBinding::inflate,
) {

    override val viewModel: HomeViewModel by viewModels()
}