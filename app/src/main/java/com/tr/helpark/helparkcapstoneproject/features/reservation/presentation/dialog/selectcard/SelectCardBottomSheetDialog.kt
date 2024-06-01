package com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog.selectcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.DialogBottomSheetSelectCardBinding
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CardUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.presentation.dialog.IAddBalanceActions
import com.tr.helpark.helparkcapstoneproject.features.profile.presentation.dialog.balance.AddBalanceBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.adapter.SelectCardAdapter

class SelectCardBottomSheetDialog(
    private val iAddBalanceActions: IAddBalanceActions,
    private val userCardList: List<CardUiModel>
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetSelectCardBinding

    private val adapter: SelectCardAdapter by lazy {
        SelectCardAdapter(::getSelectedCard)
    }

    private var selectedCard: CardUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetSelectCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(false)
        binding.rvSavedCards.adapter = adapter

        setAdapter()
        initListener()
    }

    private fun setAdapter() {
        adapter.submitList(userCardList)
    }

    private fun initListener() {
        with(binding) {
            ivCloseDialog.setOnClickListener {
                dismiss()
            }

            btnContinue.setOnClickListener {
                AddBalanceBottomSheetDialog(
                    iAddBalanceActions
                ).show(parentFragmentManager, "AddBalanceBottomSheetDialog.TAG")
                dismiss()
            }
        }
    }

    private fun getSelectedCard(cardUiModel: CardUiModel) {
        selectedCard = cardUiModel
    }

}