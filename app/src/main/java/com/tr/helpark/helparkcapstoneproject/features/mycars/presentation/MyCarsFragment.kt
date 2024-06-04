package com.tr.helpark.helparkcapstoneproject.features.mycars.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentMyCarsBinding
import com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.dialog.RemoveOptionBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.dialog.RemoveOptionBottomSheetDialog.Companion.OPERATION_REMOVE_CAR
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.AddNewCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.RemoveCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.AddCarApiState
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.uimodel.RemoveCarApiState
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.adapter.MyCarsListAdapter
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.dialog.AddNewCarBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model.FuelType
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileApiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyCarsFragment : BaseFragment<MyCarsViewModel, FragmentMyCarsBinding>(
    FragmentMyCarsBinding::inflate
) {
    override val viewModel: MyCarsViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val adapter: MyCarsListAdapter by lazy {
        MyCarsListAdapter(::onDeleteCarClickAction)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        setAdapter()
        observeLiveData()

        collectPageState(viewModel.pageStateFlow) { state ->
            when (state.pageEvent) {
                MyCarsViewModel.PageEvent.INITIAL -> {}

                MyCarsViewModel.PageEvent.ADD_NEW_CAR_RESPONSE_RECEIVED -> {
                    onAddCarResponseReceived(state.addCarApiState)
                }

                MyCarsViewModel.PageEvent.REMOVE_CAR_RESPONSE_RECEIVED -> {
                    onRemoveCarResponseReceived(state.removeCarApiState)
                }

                MyCarsViewModel.PageEvent.GET_PROFILE_RESPONSE_RECEIVED -> {
                    onGetProfileResponseReceived(state.getProfileApiState)
                }
            }
        }
    }

    private fun onGetProfileResponseReceived(getProfileApiState: GetProfileApiState) {
        when (getProfileApiState) {
            GetProfileApiState.Initial -> {}

            is GetProfileApiState.Success -> {
                viewModel.getUserCars()
            }

            is GetProfileApiState.Error -> {
                handleNetworkError(getProfileApiState.error)
            }
        }
    }

    private fun onRemoveCarResponseReceived(removeCarApiState: RemoveCarApiState) {
        when (removeCarApiState) {
            RemoveCarApiState.Initial -> {}

            is RemoveCarApiState.Success -> {
                preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                    viewModel.getProfile(userId)
                }
            }

            is RemoveCarApiState.Error -> {
                handleNetworkError(removeCarApiState.error)
            }
        }
    }

    private fun onAddCarResponseReceived(addCarApiState: AddCarApiState) {
        when (addCarApiState) {
            AddCarApiState.Initial -> {}

            is AddCarApiState.Success -> {
                preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                    viewModel.getProfile(userId)
                }
            }

            is AddCarApiState.Error -> {
                handleNetworkError(addCarApiState.error)
            }
        }
    }

    private fun setAdapter() {
        binding.rvMyCars.adapter = adapter
    }

    private fun observeLiveData() {
        viewModel.carList.observe(viewLifecycleOwner) {
            binding.rvMyCars.isVisible = it.isNotEmpty()
            binding.clEmptyState.isVisible = it.isEmpty()
            adapter.submitList(it)
        }
    }

    private fun initListeners() {
        with(binding) {
            btnAdd.setOnClickListener {
                preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                    AddNewCarBottomSheetDialog(
                        userId.toInt(),
                        FuelType.entries,
                        ::onNewCarAdded
                    ).show(
                        childFragmentManager,
                        "TAG"
                    )
                }
            }

            toolbar.tvToolbarTitle.text = getString(R.string.saved_cars)

            toolbar.icBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun onNewCarAdded(addNewCarRequestDto: AddNewCarRequestDto) {
        viewModel.addCar(addNewCarRequestDto)
    }

    private fun onDeleteCarClickAction(plate: String) {
        RemoveOptionBottomSheetDialog(OPERATION_REMOVE_CAR,onRemoveClick = {
            viewModel.removeCar(RemoveCarRequestDto(plate.trim().lowercase()))

        }).show(childFragmentManager, "SavedCardsOptionBottomSheetDialog")
    }
}