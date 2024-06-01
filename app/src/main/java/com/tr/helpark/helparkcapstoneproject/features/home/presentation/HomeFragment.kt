package com.tr.helpark.helparkcapstoneproject.features.home.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.helpark.helpark.common.utils.preferences.PreferencesKeys.KEY_USER_ID
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.handleViewVisibilityWithTranslationXEnd
import com.tr.helpark.helparkcapstoneproject.common.extensions.handleViewVisibilityWithTranslationYTop
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.redirectUserToGoogleMaps
import com.tr.helpark.helparkcapstoneproject.common.extensions.setCarParkSavedStatus
import com.tr.helpark.helparkcapstoneproject.common.extensions.setParkDensityStatus
import com.tr.helpark.helparkcapstoneproject.common.extensions.setParkIsOpenStatus
import com.tr.helpark.helparkcapstoneproject.common.extensions.setTextViewAlphaAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.util.MapActions
import com.tr.helpark.helparkcapstoneproject.common.util.MapUtils
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType
import com.tr.helpark.helparkcapstoneproject.common.util.UiActions
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentHomeBinding
import com.tr.helpark.helparkcapstoneproject.features.home.data.dto.request.ToggleFavoriteParkRequestDto
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksApiState
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModel
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.ToggleFavoriteApiState
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.ToggleFavoriteUiModel
import com.tr.helpark.helparkcapstoneproject.features.main.MapsActivity
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.FavouriteUiModel
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileApiState
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import com.tr.helpark.helparkcapstoneproject.features.search.presentation.SearchBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    FragmentHomeBinding::inflate,
) {

    override val viewModel: HomeViewModel by viewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private var shouldHideBottomSheet: Boolean = false
    private var isParkNotFoundState: Boolean = false

    private lateinit var mapActions: MapActions

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private var userId = -1

    private var favoriteParkList = listOf<FavouriteUiModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMapActions()
        setBottomSheetBehaviour()
        overrideHomeUiActions()
        initListeners()
        setUserId()

        observeMarkerPosition()
        observeLiveData()

        preferencesManager.getString(KEY_USER_ID)?.let { userId ->
            viewModel.getProfile(userId)
        }

        collectPageState(viewModel.pageStateFlow) {
            when (it.pageEvent) {
                HomeViewModel.PageEvent.INITIAL -> {
                    viewModel.getAllParks()
                }

                HomeViewModel.PageEvent.GET_ALL_PARKS_RESPONSE_RECEIVED -> {
                    onGetAllParksResponseReceived(it.registerApiState)
                }

                HomeViewModel.PageEvent.GET_PROFILE_RESPONSE_RECEIVED -> {
                    onGetProfileResponseReceived(it.getProfileApiState)
                }

                HomeViewModel.PageEvent.TOGGLE_FAVORITE_RESPONSE_RECEIVED -> {
                    onToggleFavoriteResponseReceived(it.toggleFavoriteApiState)
                }

                HomeViewModel.PageEvent.NAVIGATE_TO_NEXT_SCREEN -> {
                    onGetProfileResponseReceived(it.getProfileApiState)
                }
            }
        }
    }

    private fun observeLiveData(){
        viewModel.favoriteParkList.observe(viewLifecycleOwner){ favoriteParks ->
            favoriteParks?.let {
                favoriteParkList = it
            }
        }
    }

    private fun onGetAllParksResponseReceived(getAllParksApiState: GetAllParksApiState) {
        when (getAllParksApiState) {
            is GetAllParksApiState.Initial -> {}

            is GetAllParksApiState.Success -> {
                onGetAllParksSuccess(getAllParksApiState.uiModel)
            }

            is GetAllParksApiState.Error -> {
                handleNetworkError(getAllParksApiState.error)
            }
        }
    }

    private fun onGetProfileResponseReceived(getProfileApiState: GetProfileApiState) {
        when (getProfileApiState) {
            is GetProfileApiState.Initial -> {}

            is GetProfileApiState.Success -> {
                onGetProfileSuccess(getProfileApiState.uiModel)
            }

            is GetProfileApiState.Error -> {
                handleNetworkError(getProfileApiState.error)
            }
        }
    }

    private fun onToggleFavoriteResponseReceived(toggleFavoriteApiState: ToggleFavoriteApiState) {
        when (toggleFavoriteApiState) {
            is ToggleFavoriteApiState.Initial -> {}

            is ToggleFavoriteApiState.Success -> {
                onToggleFavoriteSuccess(toggleFavoriteApiState.uiModel)
            }

            is ToggleFavoriteApiState.Error -> {
                handleNetworkError(toggleFavoriteApiState.error)
            }
        }
    }

    private fun onToggleFavoriteSuccess(uiModel: ToggleFavoriteUiModel?) {
        uiModel?.let {
            binding.layoutParkInfo.ivSaveCarPark.setCarParkSavedStatus(it.isFavorite)

            preferencesManager.getString(KEY_USER_ID)?.let { userId ->
                viewModel.getProfile(userId)
            }
        }
    }

    private fun onGetAllParksSuccess(uiModel: GetAllParksUiModel?) {
        uiModel.let {
            it?.parks.let { parkList ->
                handleParksNotFoundViewState(parkList?.isEmpty() ?: true)
                it?.parks?.let { parks ->
                    viewModel.parkList = parks
                    mapActions.setParksToMap(parks)
                }
            }
        }
    }

    private fun onGetProfileSuccess(uiModel: GetProfileUiModel?) {
        uiModel?.let {
            binding.tbHomePage.tvName.text = it.name.toString()
        }
        viewModel.getFavoriteParks()
    }

    private fun initListeners() {
        with(binding) {
            layoutParkInfo.layoutPark.tvParkName.isSelected = true

            tbHomePage.btnSettings.setOnClickListener {
                viewModel.navigateToNextScreen()
                navigateWithAnimation(R.id.action_homeFragment_to_settingsFragment)
            }

            btnCurrentLocation.setOnClickListener {
                handleParksNotFoundViewState(isParkNotFoundState)
                (activity as? MapsActivity)?.getCurrentLocationAndMoveCamera()
                viewModel.getAllParks()
            }

            btnSearch.setOnClickListener {
                SearchBottomSheetDialogFragment().show(
                    childFragmentManager,
                    "SearchBottomSheetDialogFragment"
                )
            }

            tbHomePage.ivProfile.setOnClickListener {
                navigateWithAnimation(R.id.action_homeFragment_to_profileFragment)
            }
        }
    }

    private fun handleParksNotFoundViewState(state: Boolean) {
        with(binding) {
            isParkNotFoundState = state
            bottomSheetBehavior.isDraggable = !state

            if (state) expandBottomSheetDialog() else collapseBottomSheetDialog()
            layoutParkNotFound.root.isVisible = state
            layoutParkInfo.root.isVisible = !state
        }
    }

    private fun overrideHomeUiActions() {
        (activity as? MapsActivity)?.uiActions = object : UiActions.Home {
            override fun collapseBottomSheet() {
                collapseBottomSheetDialog()
            }

            override fun expandBottomSheet() {
                expandBottomSheetDialog()
            }

            override fun setHelperViewsVisibility(enabled: Boolean) {
                setViewVisibilityWhenMapScroll(enabled)
            }

            override fun onMarkerClickAction(marker: Marker) {
                viewModel.lastClickedMarkerId = marker.tag.toString().toInt()
                if (viewModel.lastClickedMarkerId != USER_LOCATION_MARKER_TAG_ID) {
                    collapseBottomSheetDialog()

                    val park = viewModel.parkList[viewModel.lastClickedMarkerId]
                    showCarParkDetail(park)
                    binding.layoutParkInfo.tvDistrictName.text = park.district
                } else {
                    if (!isParkNotFoundState) {
                        collapseBottomSheetDialog()
                    }
                }
            }
        }
    }

    private fun showCarParkDetail(park: GetAllParksUiModelItem) {
        with(binding) {
            var isSaved = isCarParkSaved(park.id)
            layoutParkInfo.apply {
                ivSaveCarPark.setCarParkSavedStatus(isSaved)
                tvTransactionAmount.setParkDensityStatus(park)
                layoutPark.tvParkName.text = park.parkName
                tvUpdatedTime.text = park.parkDetail?.updateDate
                tvWorkHours2.text = park.workHours
                tvAddress.text = park.parkDetail?.address
                tvParkType.text = park.parkType
                tvIsOpen.setParkIsOpenStatus(park.isOpened ?: false)
                tvIsOpen.setTextViewAlphaAnimation()
                tvLive.setTextViewAlphaAnimation()
                tableLayoutSchedule.removeAllViews()

                ivDirection.setOnClickListener {
                    navigateToMapsNavigation(
                        LatLng(
                            park.lat?.toDouble() ?: 0.0,
                            park.lng?.toDouble() ?: 0.0
                        )
                    )
                }

                ivSaveCarPark.setOnClickListener {
                    isSaved = if (isSaved) {
                        viewModel.toggleFavorite(ToggleFavoriteParkRequestDto(userId, park.id!!))
                        false
                    } else {
                        showToastMessage(
                            getString(R.string.car_park_saved_success),
                            toastType = ToastMessageType.GENERAL_SUCCESS
                        )

                        viewModel.toggleFavorite(ToggleFavoriteParkRequestDto(userId, park.id!!))
                        true
                    }

                    ivSaveCarPark.setCarParkSavedStatus(isSaved)
                }
            }
            //setParkSchedule(parkDetail, parkDetail.parkDetail?.workHours)
            expandBottomSheetDialog()
        }
    }

    private fun navigateToMapsNavigation(location: LatLng) {
        showToastMessage(
            getString(R.string.wish_good_journey),
            toastType = ToastMessageType.DIRECTION
        )

        requireContext().redirectUserToGoogleMaps(
            LatLng(
                location.latitude,
                location.longitude
            )
        )
    }

    internal fun setViewVisibilityWhenMapScroll(scrolling: Boolean) {
        handleViewVisibilityWithTranslationXEnd(
            binding.btnSearch,
            binding.btnCurrentLocation,
            show = !scrolling
        )

        handleViewVisibilityWithTranslationYTop(
            binding.tbHomePage.root,
            show = !scrolling
        )

        val isKeyboardVisible = ViewCompat.getRootWindowInsets(requireActivity().window.decorView)
            ?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
        if (isKeyboardVisible) {
            WindowCompat.getInsetsController(
                requireActivity().window,
                requireActivity().window.decorView
            ).also {
                it.hide(WindowInsetsCompat.Type.ime())
            }
        }
    }

    internal fun expandBottomSheetDialog() {
        lifecycleScope.launch {
            delay(100)
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                shouldHideBottomSheet = false
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
    }

    internal fun collapseBottomSheetDialog() {
        shouldHideBottomSheet = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setMapActions() {
        (activity as? MapsActivity)?.mapActions?.let {
            mapActions = it
        }
    }

    private fun observeMarkerPosition() {
        viewLifecycleOwner.lifecycleScope.launch {
            MapUtils.markerLocation.collect {
                if (it.latitude == 0.0 && it.longitude == 0.0)
                    return@collect

                viewModel.getAllParks()
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setBottomSheetBehaviour() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.frameLayoutBottomSheet)
        bottomSheetBehavior.apply {
            this.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.isFitToContents = false
            bottomSheetBehavior.halfExpandedRatio = 0.315f
            this.peekHeight = 0

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    val showStates = setOf(
                        BottomSheetBehavior.STATE_HALF_EXPANDED,
                        BottomSheetBehavior.STATE_COLLAPSED,
                        BottomSheetBehavior.STATE_SETTLING,
                    )
                    handleViewVisibilityWithTranslationXEnd(
                        binding.btnSearch,
                        binding.btnCurrentLocation,
                        show = newState in showStates
                    )
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (!shouldHideBottomSheet) {
                        val slideOffsetIsAboutToExpanded =
                            (slideOffset > 0.70 && slideOffset < 0.80)

                        val slideOffSetIsAboutToCollapse =
                            (slideOffset < 0.50 && slideOffset > 0.40)

                        if (slideOffSetIsAboutToCollapse && slideOffsetIsAboutToExpanded && state == BottomSheetBehavior.STATE_SETTLING) {
                            state = BottomSheetBehavior.STATE_HALF_EXPANDED
                        }
                    }
                }
            })
        }
    }

    private fun isCarParkSaved(carParkId: Int?): Boolean {
        return carParkId?.let {
            favoriteParkList.any {
                it.parkId == carParkId
            }
        } ?: false
    }

    private fun setUserId() {
        preferencesManager.getString(KEY_USER_ID)?.let { userId ->
            this.userId = userId.toInt()
        }
    }

    companion object {
        const val USER_LOCATION_MARKER_TAG_ID = -1
    }
}