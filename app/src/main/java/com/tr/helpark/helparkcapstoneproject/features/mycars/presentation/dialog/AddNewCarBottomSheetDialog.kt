package com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.DialogBottomSheetAddNewCarBinding
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.AddNewCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model.FuelType
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model.FuelTypeSpinnerAdapter
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model.FuelTypesListItemUiModel

class AddNewCarBottomSheetDialog(
    private val userId: Int,
    private val fuelTypes: List<FuelType>,
    private val onCarAdded: (AddNewCarRequestDto) -> Unit
) : BottomSheetDialogFragment(){

    private lateinit var binding : DialogBottomSheetAddNewCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetAddNewCarBinding.inflate(inflater, container, false)
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
                onCarAdded(getSelectedInputs())
                dismiss()
            }
        }
    }
    private fun setExpireMonthAndYearSpinner() {
        val fuelTypes = fuelTypes.map { FuelTypesListItemUiModel(it.name, it.id) }.toMutableList().also {
            it.add(0, FuelTypesListItemUiModel("Yakıt Tipi", 0))
        }

        binding.spFuelType.adapter =
            FuelTypeSpinnerAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                fuelTypes,
                resources
            )

    }

    private fun getSelectedInputs() : AddNewCarRequestDto {
        return AddNewCarRequestDto(
            userId,
            binding.etPlate.getText(),
            binding.etCarModel.getText(),
            binding.spFuelType.selectedItemPosition
        )
    }
}