package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.helpark.helpark.common.utils.preferences.PreferencesKeys.KEY_USER_ID
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentReservationHistoryBinding
import com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.dialog.RemoveOptionBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.CancelReservationApiState
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model.ReservationStatusType
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.GetReservationHistoryApiState
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.GetReservationHistoryUiModel
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.ReservationHistoryItemUiModel
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.presentation.adapter.ReservationHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReservationHistoryFragment :
    BaseFragment<ReservationHistoryViewModel, FragmentReservationHistoryBinding>(
        FragmentReservationHistoryBinding::inflate
    ) {
    override val viewModel: ReservationHistoryViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val adapter: ReservationHistoryAdapter by lazy {
        ReservationHistoryAdapter(::onDetailClick)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        setAdapter()

        collectPageState(viewModel.pageStateFlow) { state ->
            when (state.pageEvent) {
                ReservationHistoryViewModel.PageEvent.INITIAL -> {
                    preferencesManager.getString(KEY_USER_ID)?.let {
                        viewModel.getReservationHistory(it.toInt())
                    }
                }

                ReservationHistoryViewModel.PageEvent.ON_RESERVATION_HISTORY_RESPONSE_RECEIVED -> {
                    onReservationHistoryResponseReceived(state.reservationHistoryApiState)
                }

                ReservationHistoryViewModel.PageEvent.CANCEL_RESERVATION_RESPONSE_RECEIVED -> {
                    onReservationCancelResponseReceived(state.cancelReservationApiState)
                }
            }
        }
    }

    private fun onReservationCancelResponseReceived(cancelReservationApiState: CancelReservationApiState) {
        when (cancelReservationApiState) {
            is CancelReservationApiState.Initial -> {}

            is CancelReservationApiState.Success -> {
                preferencesManager.getString(KEY_USER_ID)?.let { userID ->
                    viewModel.getProfile(userID)
                }

                showToastMessage(
                    message = cancelReservationApiState.uiModel?.message ?: getString(R.string.reservation_cancelled),
                    toastType = ToastMessageType.GENERAL_SUCCESS
                )
                findNavController().navigate(R.id.homeFragment, null,
                    navOptions {
                        popUpTo(R.id.nav_graph) {
                            inclusive = true
                        }
                    })
            }

            is CancelReservationApiState.Error -> {
                handleNetworkError(cancelReservationApiState.error)
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            toolbar.icBack.setOnClickListener {
                findNavController().popBackStack()
            }

            toolbar.tvToolbarTitle.text = getString(R.string.tb_title_reservation_history)
        }
    }

    private fun setAdapter() {
        binding.rvReservationHistory.adapter = adapter
    }

    private fun onReservationHistoryResponseReceived(reservationHistoryApiState: GetReservationHistoryApiState) {
        when (reservationHistoryApiState) {

            is GetReservationHistoryApiState.Initial -> {}
            is GetReservationHistoryApiState.Success -> {
                onReservationHistorySuccess(reservationHistoryApiState.uiModel)
            }

            is GetReservationHistoryApiState.Error -> {
                handleNetworkError(reservationHistoryApiState.error)
            }
        }
    }

    private fun onReservationHistorySuccess(uiModel: GetReservationHistoryUiModel?) {
        uiModel?.let {
            adapter.submitList(it.reservations?.reversed())
        }
    }

    private fun onDetailClick(item: ReservationHistoryItemUiModel) {
        val reservationType = ReservationStatusType.fromValue(item.status!!)
        if (reservationType != ReservationStatusType.CANCELLED && reservationType != ReservationStatusType.COMPLETED) {
            RemoveOptionBottomSheetDialog(
                RemoveOptionBottomSheetDialog.OPERATION_CANCEL_RESERVATION,
                onRemoveClick = {
                    preferencesManager.getString(PreferencesKeys.KEY_USER_RESERVATION_ID)
                        ?.let { resId ->
                            viewModel.cancelReservation(resId.toInt())
                        }
                }
            ).show(childFragmentManager, "RemoveOptionBottomSheetDialog")
        }
    }
}