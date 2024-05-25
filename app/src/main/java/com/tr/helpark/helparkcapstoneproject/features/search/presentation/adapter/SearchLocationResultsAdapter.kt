package com.tr.helpark.helparkcapstoneproject.features.search.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.ItemSearchResultsBinding
import com.tr.helpark.helparkcapstoneproject.features.search.data.model.PlacePredictionModel

class SearchLocationResultsAdapter(
    private val selectLocation : (String) -> Unit
) :
    ListAdapter<PlacePredictionModel, SearchLocationResultsAdapter.ViewHolder>(
        SearchLocationResultsDiffCallback()
    ) {
    inner class ViewHolder(private val binding: ItemSearchResultsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(prediction: PlacePredictionModel) {
            binding.apply {

                if(prediction.isHistory){
                    binding.ivLocation.setImageResource(
                        R.drawable.ic_search_history
                    )
                }else{
                    binding.ivLocation.setImageResource(
                        R.drawable.ic_location_marker
                    )
                }

                root.setOnClickListener {
                    prediction.id?.let(selectLocation)
                }

                prediction.address?.let { tvTitle.text = it }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchResultsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchLocationResultsDiffCallback : DiffUtil.ItemCallback<PlacePredictionModel>() {

        override fun areItemsTheSame(
            oldItem: PlacePredictionModel,
            newItem: PlacePredictionModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: PlacePredictionModel,
            newItem: PlacePredictionModel
        ): Boolean {
            return newItem == oldItem
        }
    }
}