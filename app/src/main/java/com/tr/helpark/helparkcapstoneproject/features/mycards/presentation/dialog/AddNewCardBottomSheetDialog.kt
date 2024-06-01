package com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.DialogBottomSheetAddNewCardBinding
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.dto.request.AddCardRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model.ExpireDateListItemUiModel
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model.ExpireDateSpinnerAdapter
import java.util.Calendar

class AddNewCardBottomSheetDialog(
    private val userId : Int,
    private val onCardAdded : (AddCardRequestDto) -> Unit
): BottomSheetDialogFragment() {

    private lateinit var binding : DialogBottomSheetAddNewCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetAddNewCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(false)

        initListeners()
        setExpireMonthAndYearSpinner()
    }

    private fun initListeners(){
        with(binding){
            ivCloseDialog.setOnClickListener {
                dismiss()
            }

            btnSave.setOnClickListener {
                onCardAdded(getCardInputs())
                dismiss()
            }
        }
    }

    private fun setExpireMonthAndYearSpinner() {
        val monthUiModels =
            (1..12).map { ExpireDateListItemUiModel(it.toString()) }.toMutableList().also {
                it.add(0, ExpireDateListItemUiModel("Ay"))
            }
        binding.spMonth.adapter =
            ExpireDateSpinnerAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                monthUiModels,
                resources
            )

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val yearUiModels =
            (currentYear..currentYear + 8)
                .map { ExpireDateListItemUiModel(it.toString()) }
                .toMutableList()
                .also {
                    it.add(0, ExpireDateListItemUiModel("Yıl"))
                }

        binding.spYear.adapter =
            ExpireDateSpinnerAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                yearUiModels,
                resources
            )
    }

    private fun getCardInputs() : AddCardRequestDto {
        return AddCardRequestDto(
            userId = userId,
            cardName = binding.etCardOwner.getText(),
            cardAlias = binding.etMpCardName.getText(),
            cardNumber = binding.etCard.getText().trim(),
            cvv = binding.etCvv.text.toString(),
            cardDate = getCardMonthAndYear(),
            cardType = 1
        )
    }

    // if month less than 10, add 0 before month
    // Take lat two digit of year
    private fun getCardMonthAndYear() : String{
        val month = binding.spMonth.selectedItem
        val year = binding.spYear.selectedItem.toString().takeLast(2)

        if(month.toString().toInt() < 10){
            return "0${month}${year}"
        }

        return "${month}${year}"
    }
}