package com.tr.helpark.helparkcapstoneproject.features.mycards.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tr.helpark.helparkcapstoneproject.common.extensions.toMaskedCardNumber
import com.tr.helpark.helparkcapstoneproject.databinding.ItemUserCardBinding
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.CardUiModel

class MyCardsListAdapter(
    private val onDeleteCardClickAction : (Int) -> Unit
) :
    androidx.recyclerview.widget.ListAdapter<CardUiModel, MyCardsListAdapter.ViewHolder>(
        MyCardsDiffUtil()
    ) {

    inner class ViewHolder(private val binding: ItemUserCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cardUiModel: CardUiModel) {
            with(binding) {
                tvCardAlies.text = cardUiModel.cardAlias
                tvCardNumber.text = cardUiModel.cardNumber?.toMaskedCardNumber()

                root.setOnLongClickListener {
                    cardUiModel.id?.let { id ->
                        onDeleteCardClickAction(id)
                    }
                    true
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
            ItemUserCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}