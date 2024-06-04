package com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tr.helpark.helparkcapstoneproject.common.extensions.toMaskedCardNumber
import com.tr.helpark.helparkcapstoneproject.databinding.ItemSelectCardBinding
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CardUiModel

class SelectCardAdapter(
    private val onSelectedCard: (CardUiModel) -> Unit
) : ListAdapter<CardUiModel, SelectCardAdapter.ViewHolder>(
MyCardsDiffUtil()
) {
    private var selectedPosition: Int = -1

    inner class ViewHolder(private val binding: ItemSelectCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cardUiModel: CardUiModel,isSelected: Boolean) {
            with(binding) {
                tvCardAlies.text = cardUiModel.cardAlias
                tvCardNumber.text = cardUiModel.cardNumber?.toMaskedCardNumber()

                rbSelectedCard.isChecked = isSelected

                root.setOnClickListener {
                    val previousPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                    onSelectedCard(cardUiModel)
                }
            }
        }
    }

    class MyCardsDiffUtil : DiffUtil.ItemCallback<CardUiModel>() {
        override fun areItemsTheSame(oldItem: CardUiModel, newItem: CardUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CardUiModel, newItem: CardUiModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSelectCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),position == selectedPosition)
    }
}