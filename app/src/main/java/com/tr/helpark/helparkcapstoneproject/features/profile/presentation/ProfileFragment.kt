package com.tr.helpark.helparkcapstoneproject.features.profile.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.helpark.helpark.common.utils.preferences.PreferencesKeys.KEY_USER_PROFILE
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.extensions.toCurrencyString
import com.tr.helpark.helparkcapstoneproject.common.extensions.toPhoneNumberFormat
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentProfileBinding
import com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.dialog.RemoveOptionBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.AddBalanceRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.DeleteUserAccountRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.AddBalanceApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.AddBalanceUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.DeleteUserAccountApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.presentation.dialog.IAddBalanceActions
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog.selectcard.SelectCardBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {
    override val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private var profile: GetProfileUiModel? = null

    private lateinit var iAddBalanceActions: IAddBalanceActions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            setProfile()
        }

        viewModel.getUserCards()
        overrideAddBalanceActions()
        initListeners()

        collectPageState(viewModel.pageStateFlow) {
            when (it.pageEvent) {
                ProfileViewModel.PageEvent.INITIAL -> {
                    preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                        viewModel.getProfile(userId)
                    }
                }

                ProfileViewModel.PageEvent.ADD_BALANCE_RESPONSE_RECEIVED -> {
                    onAddBalanceResponseReceived(it.addBalanceApiState)
                }

                ProfileViewModel.PageEvent.GET_PROFILE_RESPONSE_RECEIVED -> {
                    onGetProfileResponseReceived(it.getProfileApiState)
                }

                ProfileViewModel.PageEvent.DELETE_USER_ACCOUNT_RESPONSE_RECEIVED -> {
                    onDeleteUserAccountResponseReceived(it.deleteUserAccountApiState)
                }
            }
        }
    }

    private fun onAddBalanceResponseReceived(addBalanceApiState: AddBalanceApiState) {
        when (addBalanceApiState) {
            is AddBalanceApiState.Initial -> {

            }

            is AddBalanceApiState.Success -> {
                onAddBalanceSuccess(addBalanceApiState.uiModel)
            }

            is AddBalanceApiState.Error -> {
                handleNetworkError(addBalanceApiState.error)
            }
        }
    }

    private fun onGetProfileResponseReceived(getProfileApiState: GetProfileApiState) {
        when (getProfileApiState) {
            is GetProfileApiState.Initial -> {}

            is GetProfileApiState.Success -> {
                onGetProfileSuccess(getProfileApiState.uiModel)
            }

            is GetProfileApiState.Error -> {
                handleNetworkError(getProfileApiState.error)
            }
        }
    }

    private fun onDeleteUserAccountResponseReceived(deleteUserAccountApiState: DeleteUserAccountApiState) {
        when (deleteUserAccountApiState) {
            is DeleteUserAccountApiState.Initial -> {}

            is DeleteUserAccountApiState.Success -> {
                showToastMessage(
                    getString(R.string.account_deleted),
                    toastType = ToastMessageType.GENERAL_SUCCESS
                )
                preferencesManager.clear()
                findNavController().navigate(R.id.loginFragment, null,
                    navOptions {
                        popUpTo(R.id.nav_graph) {
                            inclusive = true
                        }
                    })
            }

            is DeleteUserAccountApiState.Error -> {
                handleNetworkError(deleteUserAccountApiState.error)
            }
        }
    }

    private fun onAddBalanceSuccess(uiModel: AddBalanceUiModel?) {
        uiModel?.let {
            showToastMessage(
                getString(R.string.balance_added),
                toastType = ToastMessageType.GENERAL_SUCCESS
            )
        }

        preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
            viewModel.getProfile(userId)
        }
    }

    private fun onGetProfileSuccess(uiModel: GetProfileUiModel?) {
        uiModel?.let {
            updateProfile(it)
        }
    }


    private fun initListeners() {
        with(binding) {
            toolbar.icBack.setOnClickListener {
                findNavController().popBackStack()
            }

            toolbar.tvToolbarTitle.text = getString(R.string.profile_info)

            cvMyCars.setOnClickListener {
                navigateWithAnimation(R.id.action_profileFragment_to_myCarsFragment)
            }

            cvMyCards.setOnClickListener {
                navigateWithAnimation(R.id.action_profileFragment_to_myCardsFragment)
            }

            cvLogout.setOnClickListener {
                preferencesManager.clear()
                findNavController().navigate(R.id.loginFragment, null,
                    navOptions {
                        popUpTo(R.id.nav_graph) {
                            inclusive = true
                        }
                    })
            }

            clWallet.clAddBalance.setOnClickListener {
                viewModel.cardList.value?.let {
                    SelectCardBottomSheetDialog(iAddBalanceActions, it).show(
                        childFragmentManager,
                        "SelectCardBottomSheetDialog.TAG"
                    )
                }
            }

            cvDeleteAccount.setOnClickListener {
                RemoveOptionBottomSheetDialog(
                    RemoveOptionBottomSheetDialog.OPERATION_REMOVE_ACCOUNT,
                    onRemoveClick = {
                        preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                            viewModel.deleteUserAccount(DeleteUserAccountRequestDto(userId.toInt()))
                        }
                    }).show(childFragmentManager, "SavedCardsOptionBottomSheetDialog")
            }
        }
    }

    private suspend fun setProfile() {
        lifecycleScope.launch {
            profile = preferencesManager.getModel(KEY_USER_PROFILE, typeOf<GetProfileUiModel>())

            profile?.let {
                updateProfile(it)
            }
        }.join()
    }

    private fun updateProfile(getProfileUiModel: GetProfileUiModel) {
        profile = getProfileUiModel
        with(binding) {
            "${getProfileUiModel.name} ${getProfileUiModel.surname}".also { tvName.text = it }
            tvEmail.text = getProfileUiModel.email
            tvPhoneNumber.text = getProfileUiModel.phoneNumber?.toPhoneNumberFormat()

            clWallet.tvAmount.text = getProfileUiModel.balance?.toCurrencyString()
        }
    }

    private fun overrideAddBalanceActions() {
        iAddBalanceActions = object : IAddBalanceActions {
            override fun onBalanceAdded(addBalanceRequestDto: AddBalanceRequestDto) {
                viewModel.addBalance(addBalanceRequestDto)
            }
        }
    }
}