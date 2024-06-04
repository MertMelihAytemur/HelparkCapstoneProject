package com.tr.helpark.helparkcapstoneproject.features.home.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.collection.ArrayMap
import androidx.core.content.ContextCompat
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
import com.helpark.helpark.common.utils.preferences.PreferencesKeys.KEY_USER_RESERVATION_ID
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.animateAlpha
import com.tr.helpark.helparkcapstoneproject.common.extensions.handleViewVisibilityWithTranslationXEnd
import com.tr.helpark.helparkcapstoneproject.common.extensions.handleViewVisibilityWithTranslationYTop
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.redirectUserToGoogleMaps
import com.tr.helpark.helparkcapstoneproject.common.extensions.setCarParkSavedStatus
import com.tr.helpark.helparkcapstoneproject.common.extensions.setParkDensityStatus
import com.tr.helpark.helparkcapstoneproject.common.extensions.setParkIsOpenStatus
import com.tr.helpark.helparkcapstoneproject.common.extensions.setTextViewAlphaAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.helper.FirebaseHelper
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
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.dto.request.AddReservationRequestDto
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.AddReservationApiState
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.AddReservationUiModel
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.uimodel.CancelReservationApiState
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog.IAddReservationAction
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.dialog.selectcard.SelectCarBottomSheetDialog
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model.ReservationModel
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model.ReservationStatusType
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

    @Inject
    lateinit var firebaseHelper: FirebaseHelper

    private var userID = -1

    private var favoriteParkList = listOf<FavouriteUiModel>()

    private lateinit var iAddReservationAction: IAddReservationAction

    private var shouldCancelReservation = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMapActions()
        setBottomSheetBehaviour()
        overrideHomeUiActions()
        initListeners()
        setUserId()
        listenReservationStatus()
        overrideAddReservationsActions()

        observeMarkerPosition()
        observeLiveData()

        viewModel.getUserCars()
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

                HomeViewModel.PageEvent.RESERVATION_ADDED_RESPONSE_RECEIVED -> {
                    onReservationAddedResponseReceived(it.addReservationApiState)
                }

                HomeViewModel.PageEvent.CANCEL_RESERVATION_RESPONSE_RECEIVED -> {
                    onReservationCancelResponseReceived(it.cancelReservationApiState)
                }
            }
        }
    }

    private fun observeLiveData() {
        viewModel.favoriteParkList.observe(viewLifecycleOwner) { favoriteParks ->
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

    private fun onReservationAddedResponseReceived(addReservationApiState: AddReservationApiState) {
        when (addReservationApiState) {
            is AddReservationApiState.Initial -> {}

            is AddReservationApiState.Success -> {
                onReservationAddedSuccess(addReservationApiState.uiModel)
            }

            is AddReservationApiState.Error -> {
                handleNetworkError(addReservationApiState.error)
            }
        }
    }

    private fun onReservationCancelResponseReceived(cancelReservationApiState: CancelReservationApiState) {
        when (cancelReservationApiState) {
            is CancelReservationApiState.Initial -> {}

            is CancelReservationApiState.Success -> {
                collapseBottomSheetDialog()
                showToastMessage(
                    getString(R.string.reservation_cancelled_success),
                    toastType = ToastMessageType.GENERAL_SUCCESS
                )
                (activity as MapsActivity).viewModel.cancelReservation()
            }

            is CancelReservationApiState.Error -> {
                handleNetworkError(cancelReservationApiState.error)
            }
        }
    }

    private fun onReservationAddedSuccess(uiModel: AddReservationUiModel?) {
        uiModel?.let {
            preferencesManager.putString(KEY_USER_RESERVATION_ID, it.resId.toString())
        }

        (activity as? MapsActivity)?.viewModel?.createReservation()
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

                (activity as MapsActivity).startTimer(20000)
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

            layoutParkInfo.btnAddReservation.setOnClickListener {
                if (shouldCancelReservation) {
                    preferencesManager.getString(KEY_USER_RESERVATION_ID)?.let { resId ->
                        viewModel.cancelReservation(resId.toInt())
                    }
                } else {
                    lifecycleScope.launch {
                        collapseBottomSheetDialog()

                        delay(350)
                        viewModel.carList.value?.let { carList ->
                            SelectCarBottomSheetDialog(
                                iAddReservationAction,
                                carList
                            ).show(
                                childFragmentManager,
                                "SelectCarBottomSheetDialog"
                            )
                        }
                    }
                }
            }

            reservationStatusView.setCancelOrCompletedCallBack {
                (activity as MapsActivity).viewModel.removeReservationReference()
            }

            reservationStatusView.setPendingOrApprovedCallBack {
                showToastMessage(
                    "Open Detail Bottom Sheet",
                    toastType = ToastMessageType.GENERAL_SUCCESS
                )
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
                        viewModel.toggleFavorite(ToggleFavoriteParkRequestDto(userID, park.id!!))
                        false
                    } else {
                        showToastMessage(
                            getString(R.string.car_park_saved_success),
                            toastType = ToastMessageType.GENERAL_SUCCESS
                        )

                        viewModel.toggleFavorite(ToggleFavoriteParkRequestDto(userID, park.id!!))
                        true
                    }

                    ivSaveCarPark.setCarParkSavedStatus(isSaved)
                }
            }
            setParkSchedule(park, park.formattedPrices)
            setReservationModel(park)
            expandBottomSheetDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setParkSchedule(
        parkDetail: GetAllParksUiModelItem,
        schedule: ArrayMap<String, String>?
    ) {
        schedule?.let { parkSchedule ->
            val sortedSchedule = parkSchedule.toSortedMap()
            sortedSchedule.forEach {
                val tableRow = LayoutInflater.from(requireContext())
                    .inflate(R.layout.table_row_park_fee_detail_item, null) as TableRow

                tableRow.findViewById<TextView>(R.id.tvRowDetailTitle).text = it.key
                tableRow.findViewById<TextView>(R.id.tvRowDetailDesc).text = "${it.value} TL"

                binding.layoutParkInfo.tableLayoutSchedule.addView(tableRow)
            }

            if (parkDetail.parkDetail?.monthlyFee.toString().isNotEmpty()) {
                val tableRowSubscription = LayoutInflater.from(requireContext())
                    .inflate(R.layout.table_row_park_fee_detail_item, null) as TableRow

                tableRowSubscription.findViewById<TextView>(R.id.tvRowDetailTitle).text =
                    getString(R.string.monthly_subscription)
                tableRowSubscription.findViewById<TextView>(R.id.tvRowDetailDesc).text =
                    "${parkDetail.parkDetail?.monthlyFee} TL"
                binding.layoutParkInfo.tableLayoutSchedule.addView(tableRowSubscription)
            }
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

        binding.reservationStatusView.animateAlpha(!scrolling)

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

                    handleViewVisibilityWithTranslationYTop(
                        binding.tbHomePage.root,
                        show = newState in showStates
                    )

                    binding.reservationStatusView.animateAlpha(newState in showStates)
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
            this.userID = userId.toInt()
        }
    }

    private fun showTopAlertMessage(
        message: String,
        clickCommentAction: (() -> Any)? = null,
        reservationType: ReservationStatusType
    ) {
        binding.reservationStatusView.apply {
            show()
            this.messageText = message
            this.reservationType = reservationType
            this.setCommentClickListener { clickCommentAction?.invoke() }
            build()
        }
    }

    private fun listenReservationStatus() {
        preferencesManager.getString(KEY_USER_ID)?.let { userId ->
            firebaseHelper.listenToReservationStatus(userId) { status ->
                when (status) {
                    ReservationStatusType.NOT_EXIST -> {
                        shouldCancelReservation = false
                        binding.layoutParkInfo.btnAddReservation.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.map_ocean_blue
                            )
                        )
                        binding.layoutParkInfo.btnAddReservation.text =
                            getString(R.string.add_reservation)
                        binding.reservationStatusView.hide()
                    }

                    ReservationStatusType.CANCELLED -> {
                        shouldCancelReservation = false
                        binding.layoutParkInfo.btnAddReservation.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.map_ocean_blue
                            )
                        )
                        binding.layoutParkInfo.btnAddReservation.text =
                            getString(R.string.add_reservation)
                        showTopAlertMessage(
                            "Rezervasyon İptal Edildi",
                            reservationType = status
                        )
                        //Show toast message and remove user from database
                    }

                    ReservationStatusType.PENDING -> {
                        shouldCancelReservation = true
                        binding.layoutParkInfo.btnAddReservation.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        binding.layoutParkInfo.btnAddReservation.text =
                            getString(R.string.cancel_reservation)
                        showTopAlertMessage(
                            "Rezervasyon Bekleniyor",
                            reservationType = status
                        )
                    }

                    ReservationStatusType.CONFIRMED -> {
                        shouldCancelReservation = false
                        binding.layoutParkInfo.btnAddReservation.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        binding.layoutParkInfo.btnAddReservation.text =
                            getString(R.string.cancel_reservation)
                        showTopAlertMessage(
                            "Rezervasyon Onaylandı",
                            reservationType = status
                        )
                    }

                    ReservationStatusType.COMPLETED -> {
                        shouldCancelReservation = false
                        binding.layoutParkInfo.btnAddReservation.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.map_ocean_blue
                            )
                        )
                        binding.layoutParkInfo.btnAddReservation.text =
                            getString(R.string.add_reservation)
                        showTopAlertMessage(
                            "Rezervasyon Tamamlandı",
                            reservationType = status
                        )
                        //Show toast message and remove user from database
                    }
                }
            }
        }
    }

    private fun overrideAddReservationsActions() {
        iAddReservationAction = object : IAddReservationAction {
            override fun addReservation(addReservationRequestDto: AddReservationRequestDto) {
                viewModel.addReservation(addReservationRequestDto)
            }
        }
    }

    private fun setReservationModel(park: GetAllParksUiModelItem) {
        ReservationModel.apply {
            userId = userID
            parkId = park.id!!
            resTime = park.resTime!!
            hire = park.hire?.toFloat()!!
        }
    }

    companion object {
        const val USER_LOCATION_MARKER_TAG_ID = -1
    }
}