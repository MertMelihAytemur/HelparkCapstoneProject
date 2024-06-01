package com.tr.helpark.helparkcapstoneproject.features.reservation.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentReservationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReservationFragment : BaseFragment<ReservationViewModel,FragmentReservationBinding>(
    FragmentReservationBinding::inflate
) {

    override val viewModel : ReservationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}