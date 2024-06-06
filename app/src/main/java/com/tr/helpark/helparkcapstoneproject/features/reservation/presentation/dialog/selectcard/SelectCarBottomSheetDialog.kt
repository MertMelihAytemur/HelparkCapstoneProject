package com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog.selectcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.databinding.DialogBottomSheetSelectCarBinding
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CarPlateUiModel
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.adapter.SelectCarAdapter
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog.IAddReservationAction
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog.reservation.AddReservationBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model.ReservationModel
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model.resetReservationModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCarBottomSheetDialog(
    private val iAddReservationActions: IAddReservationAction,
    private val userCarList: List<CarPlateUiModel>,
    private val reservationDetail: GetAllParksUiModelItem
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetSelectCarBinding

    private val adapter: SelectCarAdapter by lazy {
        SelectCarAdapter(::getSelectedCar)
    }

    private var selectedCarPlateId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetSelectCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(false)
        binding.rvSavedCars.adapter = adapter

        initUi()
        setAdapter()
        initListener()
    }

    private fun initUi() {
        binding.rvSavedCars.isVisible = userCarList.isNotEmpty()
        binding.tvNoCardFound.isVisible = userCarList.isEmpty()
        binding.btnContinue.text =
            if (userCarList.isEmpty()) getString(R.string.add_car)
            else getString(R.string.btn_continue)
    }

    private fun setAdapter() {
        adapter.submitList(userCarList)
    }

    private fun initListener() {
        with(binding) {
            ivCloseDialog.setOnClickListener {
                resetReservationModel()
                dismiss()
            }

            btnContinue.setOnClickListener {
                if (userCarList.isNotEmpty()) {
                    AddReservationBottomSheetDialog(iAddReservationActions, reservationDetail).show(
                        parentFragmentManager,
                        AddReservationBottomSheetDialog::class.java.simpleName
                    )
                } else {
                    navigateWithAnimation(R.id.myCarsFragment)
                }
                dismiss()
            }
        }
    }

    private fun getSelectedCar(carPlateId: Int) {
        selectedCarPlateId = carPlateId
        ReservationModel.carPlateId = selectedCarPlateId
    }
}