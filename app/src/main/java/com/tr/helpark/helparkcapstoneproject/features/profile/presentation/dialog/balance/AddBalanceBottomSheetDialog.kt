package com.tr.helpark.helparkcapstoneproject.features.profile.presentation.dialog.balance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType
import com.tr.helpark.helparkcapstoneproject.databinding.DialogBottomSheetAddBalanceBinding
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.request.AddBalanceRequestDto
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.AddBalanceApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.AddBalanceUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.presentation.ProfileFragment
import com.tr.helpark.helparkcapstoneproject.features.profile.presentation.dialog.IAddBalanceActions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddBalanceBottomSheetDialog(
    private val iAddBalanceActions: IAddBalanceActions
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetAddBalanceBinding

    private val viewModel: AddBalanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetAddBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(false)

        initListeners()
        observeEvents()
    }

    private fun initListeners() {
        with(binding) {
            ivCloseDialog.setOnClickListener {
                dismiss()
            }

            btnContinue.setOnClickListener {
                viewModel.getUserId()?.let { userId ->
                    val amount = edtAmount.text.toString().trim()
                    if (amount.isNotEmpty()) {
                        iAddBalanceActions.onBalanceAdded(
                            AddBalanceRequestDto(
                                userId.toInt(),
                                amount.toFloat()
                            )
                        )
                        dismiss()
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pageStateFlow.collect { pageState ->
                when (pageState.pageEvent) {
                    AddBalanceViewModel.PageEvent.INITIAL -> {}

                    AddBalanceViewModel.PageEvent.ADD_BALANCE_RESPONSE_RECEIVED -> {
                        addBalanceResponseReceived(pageState.addBalanceApiState)
                    }
                }
            }
        }
    }

    private fun addBalanceResponseReceived(addBalanceApiState: AddBalanceApiState) {
        when (addBalanceApiState) {
            is AddBalanceApiState.Initial -> {}
            is AddBalanceApiState.Success -> {
                onAddBalanceSuccess(addBalanceApiState.uiModel)
            }

            is AddBalanceApiState.Error -> {
                showToastMessage(
                    addBalanceApiState.error.toString() ?: "Bir hata oluştu",
                    toastType = ToastMessageType.GENERAL_ERROR
                )
            }
        }
    }

    private fun onAddBalanceSuccess(uiModel: AddBalanceUiModel?) {
        uiModel?.let {
            showToastMessage(
                "Bakiye Hesabınıza Yüklendi",
                toastType = ToastMessageType.GENERAL_SUCCESS
            )
        }
        setFragmentResult(ProfileFragment.KEY_SHOULD_REFRESH, Bundle().apply {
            putBoolean(ProfileFragment.KEY_SHOULD_REFRESH, true)
        })
        dismiss()
    }
}
