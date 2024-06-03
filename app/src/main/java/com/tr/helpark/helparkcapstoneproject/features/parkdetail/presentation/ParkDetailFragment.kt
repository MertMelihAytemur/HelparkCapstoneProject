package com.tr.helpark.helparkcapstoneproject.features.parkdetail.presentation

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.customview.MapInfoWindowAdapter
import com.tr.helpark.helparkcapstoneproject.common.extensions.parcelable
import com.tr.helpark.helparkcapstoneproject.common.extensions.redirectUserToGoogleMaps
import com.tr.helpark.helparkcapstoneproject.common.extensions.setParkDensityStatus
import com.tr.helpark.helparkcapstoneproject.common.extensions.setParkIsOpenStatus
import com.tr.helpark.helparkcapstoneproject.common.extensions.setTextViewAlphaAnimation
import com.tr.helpark.helparkcapstoneproject.common.extensions.showToastMessage
import com.tr.helpark.helparkcapstoneproject.common.util.Constants
import com.tr.helpark.helparkcapstoneproject.common.util.ToastMessageType
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentParkDetailBinding
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.uimodel.GetFavoritesUiModelItem

class ParkDetailFragment : BaseFragment<ParkDetailViewModel, FragmentParkDetailBinding>(
    FragmentParkDetailBinding::inflate
), OnMapReadyCallback {

    override val viewModel: ParkDetailViewModel by viewModels()

    private var parkDetailItem: GetFavoritesUiModelItem? = null

    private lateinit var mMap: GoogleMap

    private lateinit var carParkLocationLatLng: LatLng

    private var carParkName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setArguments()
        initListeners()
        setLiveTextAlphaAnimation()
        initSupportMapFragment()

        parkDetailItem?.let { park ->
            showCarParkDetail(park)
        }
    }

    private fun initListeners() {
        with(binding) {
            btnDirections.setOnClickListener {
                parkDetailItem?.let { park ->
                    navigateToMapsNavigation(
                        LatLng(
                            park.lat?.toDouble() ?: 0.0,
                            park.lng?.toDouble() ?: 0.0
                        )
                    )
                }
            }
        }
    }

    private fun initSupportMapFragment() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.fParkMapLocation) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setArguments() {
        parkDetailItem =
            arguments?.parcelable<GetFavoritesUiModelItem>(KEY_ARGUMENT_PARK_DETAIL_ITEM)
    }

    private fun setLiveTextAlphaAnimation() {
        with(binding) {
            val liveAnimation = AlphaAnimation(0.1f, 1.0f)
            liveAnimation.duration = 600
            liveAnimation.repeatCount = Animation.INFINITE
            liveAnimation.repeatMode = Animation.REVERSE
            tvIsOpen.startAnimation(liveAnimation)
        }
    }

    private fun showCarParkDetail(park: GetFavoritesUiModelItem) {
        carParkLocationLatLng = LatLng(park.lat?.toDouble() ?: 0.0, park.lng?.toDouble() ?: 0.0)
        carParkName = park.parkName.toString()

        showParkLocationOnMap(park)
        binding.apply {
            tvTransactionAmount.setParkDensityStatus(park)
            toolbar.tvToolbarTitle.text = park.parkName
            tvUpdatedTime.text = park.parkDetail?.updateDate
            tvWorkHours2.text = park.workHours
            tvAddress.text = park.parkDetail?.address
            tvParkType.text = park.parkType
            tvIsOpen.setParkIsOpenStatus(park.isOpened ?: false)
            tvIsOpen.setTextViewAlphaAnimation()
            tableLayoutSchedule.removeAllViews()
        }

    }

    private fun showParkLocationOnMap(park: GetFavoritesUiModelItem) {
        mMap.clear()
        mMap.addMarker(
            MarkerOptions().position(carParkLocationLatLng).title(
                park.parkName
            )
        )?.apply {
            tag = -1
        }?.showInfoWindow()

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(carParkLocationLatLng, 13f))
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setInfoWindowAdapter(MapInfoWindowAdapter(requireContext()))

        // map style
        val styleResource = if (Constants.isDay) R.raw.map_style_day else R.raw.map_style_day
        val mapStyle = MapStyleOptions.loadRawResourceStyle(requireContext(), styleResource)
        mMap.setMapStyle(mapStyle)

        setMapUiSettings()
    }

    private fun setMapUiSettings() {
        mMap.uiSettings.apply {
            isCompassEnabled = false
            isMapToolbarEnabled = true
            setAllGesturesEnabled(false)
        }
    }

    companion object {
        const val KEY_ARGUMENT_PARK_DETAIL_ITEM = "PARK_DETAIL_ITEM"
    }

}