package com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.DialogBottomSheetAddReservationBinding
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.AddReservationRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog.IAddReservationAction
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model.ReservationModel

class AddReservationBottomSheetDialog(
    private val iAddReservationActions: IAddReservationAction
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetAddReservationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetAddReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(false)

        initListener()
    }

    private fun initListener(){
        binding.apply {
            btnContinue.setOnClickListener {
                iAddReservationActions.addReservation(
                    AddReservationRequestDto(
                    userId = ReservationModel.userId!!,
                    carPlateId = ReservationModel.carPlateId!!,
                    parkId = ReservationModel.parkId!!,
                    resTime = ReservationModel.resTime!!,
                    hire = ReservationModel.hire!!
                ))
                dismiss()
            }

            ivCloseDialog.setOnClickListener {
                dismiss()
            }
        }
    }
}