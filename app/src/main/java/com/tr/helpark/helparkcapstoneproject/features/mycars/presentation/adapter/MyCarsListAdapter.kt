package com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tr.helpark.helparkcapstoneproject.common.extensions.addSpacesBetweenLettersAndDigits
import com.tr.helpark.helparkcapstoneproject.databinding.ItemUserCarBinding
import com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model.FuelType
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CarPlateUiModel

class MyCarsListAdapter(
    private val onDeleteCardClickAction : (String) -> Unit
) : ListAdapter<CarPlateUiModel, MyCarsListAdapter.ViewHolder>(
    MyCarListDiffUtil()
) {

    inner class ViewHolder(private val binding: ItemUserCarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarPlateUiModel) {
            with(binding) {
                tvPlate.text = item.plate.toString().addSpacesBetweenLettersAndDigits()
                tvModel.text = item.model.toString()
                item.fuelTypeId?.let {
                    tvFuelType.text = FuelType.fromId(it)?.description
                }

                root.setOnLongClickListener {
                    item.plate?.let { plate ->
                        onDeleteCardClickAction(plate)
                    }
                    true
                }
            }
        }
    }

    class MyCarListDiffUtil(
    ) : DiffUtil.ItemCallback<CarPlateUiModel>() {
        override fun areItemsTheSame(
            oldItem: CarPlateUiModel,
            newItem: CarPlateUiModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CarPlateUiModel,
            newItem: CarPlateUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserCarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}