package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.setFormattedDate
import com.tr.helpark.helparkcapstoneproject.common.extensions.setTransactionStatus
import com.tr.helpark.helparkcapstoneproject.databinding.ItemReservationHistoryBinding
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model.ReservationStatusType
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.uimodel.ReservationHistoryItemUiModel

class ReservationHistoryAdapter(
    private val onDetailClick: (ReservationHistoryItemUiModel) -> Unit
) : ListAdapter<ReservationHistoryItemUiModel, ReservationHistoryAdapter.ViewHolder>(
    ReservationHistoryDiffCallback()
) {

    inner class ViewHolder(
        private val binding: ItemReservationHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReservationHistoryItemUiModel) = with(binding) {
            root.setOnLongClickListener {
                onDetailClick(item)
                true
            }

            chipReservationStatus.setTransactionStatus(
                ReservationStatusType.fromValue(
                    item.status ?: -2
                )
            )
            tvParkName.text = item.parkName

            item.resDate?.let {
                tvDate.setFormattedDate(it)
            }

            tvHire.text = buildString {
                append(item.hire.toString())
                append(" ")
                append(
                    tvHire.context.getString(
                        R.string.currency_value
                    )
                )
            }
        }
    }

    class ReservationHistoryDiffCallback : DiffUtil.ItemCallback<ReservationHistoryItemUiModel>() {
        override fun areItemsTheSame(
            oldItem: ReservationHistoryItemUiModel,
            newItem: ReservationHistoryItemUiModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ReservationHistoryItemUiModel,
            newItem: ReservationHistoryItemUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReservationHistoryBinding.inflate(
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