package com.tr.helpark.helparkcapstoneproject.features.favorites.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tr.helpark.helparkcapstoneproject.databinding.ItemSavedCarParkBinding
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModelItem

class FavoritesListAdapter(
    private val onShowDetailClick: (GetFavoritesUiModelItem) -> Unit,
    private val onOptionsClick: (GetFavoritesUiModelItem) -> Unit
) : ListAdapter<GetFavoritesUiModelItem, FavoritesListAdapter.ViewHolder>(
    FavoritesDiffUtilCallback()
) {
    inner class ViewHolder(
        private val binding: ItemSavedCarParkBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GetFavoritesUiModelItem) = with(binding) {
            with(binding) {
                rootView.setOnClickListener { onShowDetailClick(item) }
                ivOptions.setOnClickListener { onOptionsClick(item) }

                tvCarParkName.text = item.parkName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSavedCarParkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoritesDiffUtilCallback : DiffUtil.ItemCallback<GetFavoritesUiModelItem>() {
        override fun areItemsTheSame(
            oldItem: GetFavoritesUiModelItem,
            newItem: GetFavoritesUiModelItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GetFavoritesUiModelItem,
            newItem: GetFavoritesUiModelItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}