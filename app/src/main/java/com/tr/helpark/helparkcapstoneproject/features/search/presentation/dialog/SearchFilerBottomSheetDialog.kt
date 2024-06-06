package com.tr.helpark.helparkcapstoneproject.features.search.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.DialogBottomSheetSearchFilterBinding
import com.tr.helpark.helparkcapstoneproject.features.main.MapsActivity
import com.tr.helpark.helparkcapstoneproject.features.search.domain.uimodel.GetDistrictsItemUiModel

class SearchFilerBottomSheetDialog(
    private val districtList: List<GetDistrictsItemUiModel>
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetSearchFilterBinding

    private var currentRadius: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomSheetSearchFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as? BottomSheetDialog)?.setCanceledOnTouchOutside(false)



        initListener()
    }

    private fun initListener() {
        binding.apply {
            ivCloseDialog.setOnClickListener {
                dismiss()
            }

            setAdapter(binding.tvDistricts, districtList.map { it.district })

            btnAddReservation.setOnClickListener {
                getSelectedDistrict { disctrict ->
                    (activity as MapsActivity).apply {
                        disctrict.district ?: "empty"
                        updateRadius(currentRadius)
                        isSearchFromFilter = true

                        setCurrentLocationAndMoveCamera(
                            LatLng(
                                disctrict.lat?.toDouble() ?: 0.0,
                                disctrict.lng?.toDouble() ?: 0.0
                            )
                        )
                    }
                }
                dismiss()
            }

            rangeSlider.addOnChangeListener { slider, value, fromUser ->
                tvRadius.text = "${value.toInt()} Km"
                currentRadius = value.toDouble()
            }
        }
    }

    private fun setAdapter(
        autoCompleteTextView: AutoCompleteTextView,
        list: List<String?>
    ) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list.sortedBy { it }
        )

        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.isClickable = false
            autoCompleteTextView.showDropDown()
            autoCompleteTextView.setBackgroundResource(R.drawable.arrow_up_spinner_layer)
        }

        autoCompleteTextView.setOnDismissListener {
            autoCompleteTextView.setBackgroundResource(R.drawable.arrow_down_spinner_layer)
        }
    }

    private fun getSelectedDistrict(response: (GetDistrictsItemUiModel) -> Unit) {
        val selectedDistrict = binding.tvDistricts.text.toString()
        districtList.find { it.district == selectedDistrict }?.let {
            response(it)
        }
    }

}