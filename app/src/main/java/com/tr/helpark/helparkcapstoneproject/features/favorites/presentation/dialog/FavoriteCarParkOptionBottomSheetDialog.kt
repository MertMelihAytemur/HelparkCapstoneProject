package com.tr.helpark.helparkcapstoneproject.features.favorites.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.maps.model.LatLng
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.DialogBottomSheetFavoriteCarOptionBinding
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModelItem

class FavoriteCarParkOptionBottomSheetDialog(
    private val uiModel: GetFavoritesUiModelItem,
    private val onRemoveClick : (Int) -> Unit,
    private val onDirectionClick : (LatLng) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetFavoriteCarOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetFavoriteCarOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(false)

        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            tvTitle.text = uiModel.parkName

            btnDirections.setOnClickListener {
                onDirectionClick(LatLng(uiModel.lat?.toDouble()!!, uiModel.lng?.toDouble()!!))
                dismiss()
            }

            btnRemove.setOnClickListener {
                onRemoveClick(uiModel.id!!)
                dismiss()
            }
        }
    }
}