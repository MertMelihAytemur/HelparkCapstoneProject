package com.tr.helpark.helparkcapstoneproject.features.favorites.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.maps.model.LatLng
import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.redirectUserToGoogleMaps
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentFavoritesBinding
import com.tr.helpark.helparkcapstoneproject.features.favorites.data.dto.request.GetFavoritesRequestDto
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesApiState
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModel
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModelItem
import com.tr.helpark.helparkcapstoneproject.features.favorites.presentation.adapter.FavoritesListAdapter
import com.tr.helpark.helparkcapstoneproject.features.favorites.presentation.dialog.FavoriteCarParkOptionBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.request.ToggleFavoriteParkRequestDto
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.ToggleFavoriteApiState
import com.tr.helpark.helparkcapstoneproject.features.parkdetail.presentation.ParkDetailFragment.Companion.KEY_ARGUMENT_PARK_DETAIL_ITEM
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FavoritesViewModel,FragmentFavoritesBinding>(
    FragmentFavoritesBinding::inflate
) {
    override val viewModel : FavoritesViewModel by viewModels()

    private val adapter : FavoritesListAdapter by lazy {
        FavoritesListAdapter(
            ::onShowDetailClick,
            ::onOptionsClick
        )
    }

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMyFavorites.adapter = adapter
        binding.toolbar.tvToolbarTitle.text = getString(R.string.favorites)

        initListeners()

        collectPageState(viewModel.pageStateFlow){
            when(it.pageEvent){
                FavoritesViewModel.PageEvent.INITIAL -> {
                    preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                        viewModel.getFavorites(GetFavoritesRequestDto(userId))
                    }
                }

                FavoritesViewModel.PageEvent.GET_FAVORITES_RESPONSE_RECEIVED -> {
                    onGetFavoritesResponseReceived(it.getFavoritesApiState)
                }

                FavoritesViewModel.PageEvent.TOGGLE_FAVORITE_RESPONSE_RECEIVED -> {
                    onToggleFavoriteResponseReceived(it.toggleFavoriteApiState)
                }
            }
        }
    }

    private fun initListeners(){
        with(binding){
            toolbar.icBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun onGetFavoritesResponseReceived(getFavoritesApiState: GetFavoritesApiState){
        when(getFavoritesApiState){
            is GetFavoritesApiState.Initial -> {

            }

            is GetFavoritesApiState.Success -> {
                onGetFavoritesSuccess(getFavoritesApiState.uiModel)
            }

            is GetFavoritesApiState.Error -> {
                handleNetworkError(getFavoritesApiState.error)
            }
        }
    }

    private fun onToggleFavoriteResponseReceived(toggleFavoriteApiState: ToggleFavoriteApiState){
        when(toggleFavoriteApiState){
            is ToggleFavoriteApiState.Initial -> {

            }

            is ToggleFavoriteApiState.Success -> {
                preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
                    viewModel.getFavorites(GetFavoritesRequestDto(userId))
                }
            }

            is ToggleFavoriteApiState.Error -> {
                handleNetworkError(toggleFavoriteApiState.error)
            }
        }
    }

    private fun onGetFavoritesSuccess(uiModel: GetFavoritesUiModel?) {
        uiModel?.parks?.let {
            binding.clMyFavorites.isVisible = it.isNotEmpty()
            binding.clEmptyState.isVisible = it.isEmpty()

            adapter.submitList(it)
        }
    }

    private fun onShowDetailClick(item: GetFavoritesUiModelItem) {
        val bundle = Bundle().apply {
            putParcelable(KEY_ARGUMENT_PARK_DETAIL_ITEM, item)
        }

        navigateWithAnimation(R.id.action_favoritesFragment_to_parkDetailFragment, bundle)
    }

    private fun onOptionsClick(item: GetFavoritesUiModelItem) {
        FavoriteCarParkOptionBottomSheetDialog(
            item,
            ::onRemoveClick,
            ::onDirectionClick
        ).show(childFragmentManager, "FavoritesBottomSheetDialog.TAG")
    }

    private fun onRemoveClick(id: Int) {
        preferencesManager.getString(PreferencesKeys.KEY_USER_ID)?.let { userId ->
            viewModel.toggleFavorite(ToggleFavoriteParkRequestDto(userId.toInt(), id))
        }
    }

    private fun onDirectionClick(latLng: LatLng) {
        navigateToMapsNavigation(latLng)
    }

    private fun navigateToMapsNavigation(location: LatLng) {
        showToastMessage(
            getString(R.string.wish_good_journey),
            toastType = ToastMessageType.DIRECTION
        )

        requireContext().redirectUserToGoogleMaps(
            com.google.android.gms.maps.model.LatLng(
                location.lat,
                location.lng
            )
        )
    }

}