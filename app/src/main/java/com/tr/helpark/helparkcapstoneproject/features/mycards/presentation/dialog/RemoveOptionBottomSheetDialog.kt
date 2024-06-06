package com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.DialogBottomSheetSavedCardOptionsBinding

class RemoveOptionBottomSheetDialog(
    private val operationId : Int,
    private val onRemoveClick : () -> Unit,
) : BottomSheetDialogFragment(){
    private lateinit var binding: DialogBottomSheetSavedCardOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetSavedCardOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(true)

        when(operationId){
            OPERATION_REMOVE_CARD -> {
                binding.tvTitle.text = getString(R.string.remove_card)
            }
            OPERATION_REMOVE_CAR -> {
                binding.tvTitle.text = getString(R.string.remove_car)
            }
            OPERATION_REMOVE_ACCOUNT -> {
                binding.tvTitle.text = getString(R.string.remove_account)
            }
            OPERATION_CANCEL_RESERVATION -> {
                binding.tvTitle.text = getString(R.string.cancel_reservation)
                binding.btnRemove.text = getString(R.string.cancel)
            }
        }
    }

    private fun initListener() {
        binding.btnRemove.setOnClickListener {
            onRemoveClick()
            dismiss()
        }
    }

    companion object{
        const val OPERATION_REMOVE_CARD = 1
        const val OPERATION_REMOVE_CAR = 2
        const val OPERATION_REMOVE_ACCOUNT = 3
        const val OPERATION_CANCEL_RESERVATION = 4
    }
}