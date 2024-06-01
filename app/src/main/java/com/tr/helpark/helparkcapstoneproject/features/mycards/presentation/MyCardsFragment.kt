package com.tr.helpark.helparkcapstoneproject.features.mycards.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentMyCardsBinding
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.AddCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.RemoveCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.AddCardApiState
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.uimodel.RemoveCardApiState
import com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.adapter.MyCardsListAdapter
import com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.dialog.AddNewCardBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.dialog.RemoveOptionBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.dialog.RemoveOptionBottomSheetDialog.Companion.OPERATION_REMOVE_CARD
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileApiState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyCardsFragment : BaseFragment<MyCardsViewModel, FragmentMyCardsBinding>(
    FragmentMyCardsBinding::inflate
) {
    override val viewModel: MyCardsViewModel by viewModels()

    private val adapter: MyCardsListAdapter by lazy {
        MyCardsListAdapter(::onDeleteCardClickAction)
    }

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        setAdapter()
        observeLiveData()

        collectPageState(viewModel.pageStateFlow) { state ->
            when (state.pageEvent) {
                MyCardsViewModel.PageEvent.INITIAL -> {
                    viewModel.getUserCards()
                }

                MyCardsViewModel.PageEvent.ADD_CARD_RESPONSE_RECEIVED -> {
                    onAddCardResponseReceived(state.addCardApiState)
                }

                MyCardsViewModel.PageEvent.REMOVE_CARD_RESPONSE_RECEIVED -> {
                    onRemoveCardResponseReceived(state.removeCardApiState)
                }

                MyCardsViewModel.PageEvent.GET_PROFILE_RESPONSE_RECEIVED -> {
                    onGetProfileResponseReceived(state.getProfileApiState)
                }
            }
        }
    }

    private fun onAddCardResponseReceived(addCardApiState: AddCardApiState) {
        when (addCardApiState) {
            is AddCardApiState.Initial -> {}

            is AddCardApiState.Success -> {
                preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                    viewModel.getProfile(userId)
                }
            }

            is AddCardApiState.Error -> {

            }
        }
    }

    private fun onRemoveCardResponseReceived(removeCardApiState: RemoveCardApiState) {
        when (removeCardApiState) {
            is RemoveCardApiState.Initial -> {}

            is RemoveCardApiState.Success -> {
                preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                    viewModel.getProfile(userId)
                }
            }

            is RemoveCardApiState.Error -> {
                preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                    viewModel.getProfile(userId)
                }
            }
        }
    }

    private fun onGetProfileResponseReceived(getProfileApiState: GetProfileApiState) {
        when (getProfileApiState) {
            is GetProfileApiState.Initial -> {}

            is GetProfileApiState.Success -> {
                viewModel.getUserCards()
            }

            is GetProfileApiState.Error -> {}
        }
    }

    private fun setAdapter() {
        binding.rvMyCards.adapter = adapter
    }

    private fun initListeners() {
        with(binding) {
            toolbar.icBack.setOnClickListener { findNavController().popBackStack() }

            toolbar.tvToolbarTitle.text = getString(R.string.saved_cards)

            btnAdd.setOnClickListener {
                preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                    AddNewCardBottomSheetDialog(userId.toInt(), ::onCardAdded).show(
                        childFragmentManager,
                        "AddNewCardBottomSheetDialog"
                    )
                }
            }
        }
    }

    private fun onDeleteCardClickAction(id: Int) {
        RemoveOptionBottomSheetDialog(OPERATION_REMOVE_CARD,onRemoveClick = {
            preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                viewModel.removeCard(RemoveCardRequestDto(userId.toInt(), id))
            }
        }).show(childFragmentManager, "SavedCardsOptionBottomSheetDialog")
    }

    private fun observeLiveData() {
        viewModel.cardList.observe(viewLifecycleOwner) {
            binding.rvMyCards.isVisible = it.isNotEmpty()
            binding.clEmptyState.isVisible = it.isEmpty()
            adapter.submitList(it)
        }
    }

    private fun onCardAdded(addCardRequestDto: AddCardRequestDto) {
        viewModel.addCard(addCardRequestDto)
    }
}