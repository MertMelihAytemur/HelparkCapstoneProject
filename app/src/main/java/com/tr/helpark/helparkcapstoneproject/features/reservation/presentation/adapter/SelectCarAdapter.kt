package com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tr.helpark.helparkcapstoneproject.common.extensions.addSpacesBetweenLettersAndDigits
import com.tr.helpark.helparkcapstoneproject.databinding.ItemSelectCarBinding
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model.FuelType
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CarPlateUiModel

class SelectCarAdapter(
    private val onSelectedCar: (Int) -> Unit
) : ListAdapter<CarPlateUiModel, SelectCarAdapter.ViewHolder>(
    MyCardsDiffUtil()
) {
    private var selectedPosition: Int = -1

    inner class ViewHolder(private val binding: ItemSelectCarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(carPlateUiModel: CarPlateUiModel, isSelected: Boolean) {
            with(binding) {
                tvPlate.text = carPlateUiModel.plate.toString().addSpacesBetweenLettersAndDigits()
                tvModel.text = carPlateUiModel.model.toString()

                carPlateUiModel.fuelTypeId?.let {
                    tvFuelType.text = FuelType.fromId(it)?.description
                }

                rbSelectedCard.isChecked = isSelected

                root.setOnClickListener {
                    val previousPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                    carPlateUiModel.id?.let { it1 -> onSelectedCar(it1) }
                }
            }
        }
    }

    class MyCardsDiffUtil : DiffUtil.ItemCallback<CarPlateUiModel>() {
        override fun areItemsTheSame(oldItem: CarPlateUiModel, newItem: CarPlateUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CarPlateUiModel, newItem: CarPlateUiModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSelectCarBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),position == selectedPosition)
    }
}