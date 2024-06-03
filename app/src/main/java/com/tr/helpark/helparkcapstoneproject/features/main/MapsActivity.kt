package com.tr.helpark.helparkcapstoneproject.features.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.location.LocationManagerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.customview.MapInfoWindowAdapter
import com.tr.helpark.helparkcapstoneproject.common.extensions.findNavController
import com.tr.helpark.helparkcapstoneproject.common.extensions.logException
import com.tr.helpark.helparkcapstoneproject.common.extensions.openApplicationDetailSettings
import com.tr.helpark.helparkcapstoneproject.common.extensions.postValueIfDifferent
import com.tr.helpark.helparkcapstoneproject.common.manager.PermissionManager
import com.tr.helpark.helparkcapstoneproject.common.util.Constants
import com.tr.helpark.helparkcapstoneproject.common.util.LocationHelper
import com.tr.helpark.helparkcapstoneproject.common.util.MapActions
import com.tr.helpark.helparkcapstoneproject.common.util.MapManager
import com.tr.helpark.helparkcapstoneproject.common.util.MapUtils
import com.tr.helpark.helparkcapstoneproject.common.util.UiActions
import com.tr.helpark.helparkcapstoneproject.common.worker.TimerWorker
import com.tr.helpark.helparkcapstoneproject.core.LoadingDialog
import com.tr.helpark.helparkcapstoneproject.databinding.ActivityMapsBinding
import com.tr.helpark.helparkcapstoneproject.features.home.domain.uimodel.GetAllParksUiModelItem
import com.tr.helpark.helparkcapstoneproject.features.home.presentation.HomeFragment
import com.tr.helpark.helparkcapstoneproject.features.login.presentation.LoginFragment
import com.tr.helpark.helparkcapstoneproject.features.main.dialog.ExitApplicationDialog
import com.tr.helpark.helparkcapstoneproject.features.main.dialog.LocationPermissionNeededDialog
import com.tr.helpark.helparkcapstoneproject.features.main.dialog.TurnOnLocationServicesDialog
import com.tr.helpark.helparkcapstoneproject.features.otp.presentation.OtpVerificationFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    private lateinit var mMap: GoogleMap

    private lateinit var navController: NavController

    private val binding: ActivityMapsBinding by lazy {
        ActivityMapsBinding.inflate(layoutInflater)
    }

    var uiActions: UiActions.Home? = null

    lateinit var mapManager: MapManager

    lateinit var mapActions: MapActions

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var locationHelper: LocationHelper

    private lateinit var locationPermissionNeededDialog: LocationPermissionNeededDialog
    private lateinit var turnOnLocationServicesDialog: TurnOnLocationServicesDialog
    private lateinit var exitApplicationDialog: ExitApplicationDialog

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        // no-op
    }

    private lateinit var resolutionLauncher: ActivityResultLauncher<IntentSenderRequest>

    private val gpsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { itIntent ->
                if (itIntent.action == LocationManager.PROVIDERS_CHANGED_ACTION
                ) {
                    runCatching {
                        val locationManager =
                            getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        val result = LocationManagerCompat.isLocationEnabled(locationManager)
                        viewModel.isLocationServicesEnabled.postValueIfDifferent(result)
                        if (result) {
                            getCurrentLocationAndMoveCamera()
                        }
                    }.logException()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navController = findNavController()

        resolutionLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    getCurrentLocationAndMoveCamera()
                }
            }

        setGoogleMapsVisibility()
        initMapActions()
        initSupportMapFragment()
        initOnBackPressedDispatcher()
        observeLiveData()
        registerForGpsReceiver()
        initDialogs()
        askNotificationPermission()
    }

    private fun initMapActions() {
        mapActions = object : MapActions {
            override fun moveCamera(coordinate: LatLng, cameraPosition: CameraPosition) {
                mapManager.moveCameraToLocation(coordinate, cameraPosition)
                uiActions?.collapseBottomSheet()
                MapUtils.markerLocation.tryEmit(coordinate)
            }

            override fun setParksToMap(parkList: List<GetAllParksUiModelItem>) {
                if (::mapManager.isInitialized) {
                    mapManager.setNearestMaviShops(ArrayList(parkList))
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setInfoWindowAdapter(MapInfoWindowAdapter(this))

        mapManager = MapManager.MapManagerFactory.create(this, mMap, locationHelper)
        // map style
        val styleResource = if (Constants.isDay) R.raw.map_style_day else R.raw.map_style_day
        val mapStyle = MapStyleOptions.loadRawResourceStyle(this, styleResource)
        mMap.setMapStyle(mapStyle)

        setMaxMinZoomLevels()
        setUserLocationOnMapAndUiSettings()
        listenMapTouchEvent()

        mMap.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener {
            uiActions?.onMarkerClickAction(it)
            return@OnMarkerClickListener false
        })
    }

    /**
     * Obtain the SupportMapFragment and get notified when the map is ready to be used.
     */
    private fun initSupportMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun listenMapTouchEvent() {

        mapManager.setOnCameraMoveStartedListener { reasonCode ->
            if (reasonCode == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                uiActions?.setHelperViewsVisibility(true)
            }
        }
        mapManager.setOnCameraIdleListener {
            mapManager.setCurrentZoomToCameraPosition()
            uiActions?.setHelperViewsVisibility(false)
        }
    }

    private fun setMaxMinZoomLevels() {
        mapManager.setMinZoomPreference(8f)
        mapManager.setMaxZoomPreference(17f)
    }

    private fun setUserLocationOnMapAndUiSettings() {
        getCurrentLocationAndMoveCamera()
        mapManager.setIsCompassEnabled(false)
    }

    fun getCurrentLocationAndMoveCamera() {
        if (::mapManager.isInitialized) {
            mapManager.getUserLocationAndMoveCamera(
                onLocationSaved = { location ->
                    viewModel.userLocation = location

                    mapManager.moveCameraToLocation(location)
                    MapUtils.markerLocation.tryEmit(location)
                }, onLocationFailed = {
                    //try again if location services opened

                    getCurrentLocationAndMoveCamera()
                }
            )
        }
    }

    private fun initOnBackPressedDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (getCurrentFragment()) {
                    is HomeFragment, is LoginFragment -> moveTaskToBack(true)
                    is OtpVerificationFragment -> findNavController().navigate(R.id.loginFragment,
                        null,
                        navOptions {
                            popUpTo(R.id.nav_graph) {
                                inclusive = true
                            }
                        })

                    else -> navController.navigateUp()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun getCurrentFragment(): Fragment? {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.childFragmentManager.primaryNavigationFragment
    }

    private fun observeLiveData() {
        viewModel.isLocationServicesEnabled.observe(this) { enabled ->
            if (enabled && turnOnLocationServicesDialog.isShowing) {
                turnOnLocationServicesDialog.dismiss()
            } else {
                handleTurnOnLocationServicesDialog()
            }
        }
        viewModel.isLocationPermissionGranted.observe(this) { enabled ->
            if (enabled && locationPermissionNeededDialog.isShowing) {
                locationPermissionNeededDialog.dismiss()
            } else {
                handleLocationPermissionDialog()
            }
        }
    }

    private fun registerForGpsReceiver() {
        registerReceiver(gpsReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }

    private fun initDialogs() {
        locationPermissionNeededDialog = LocationPermissionNeededDialog(this) {
            openApplicationDetailSettings()
        }

        turnOnLocationServicesDialog = TurnOnLocationServicesDialog(this) {
            requestTurnOnLocationServices { viewModel.isLocationServicesEnabled.value = true }
        }

        exitApplicationDialog = ExitApplicationDialog(this) {
            this.finish()
        }
    }

    private fun handleTurnOnLocationServicesDialog() {
        runCatching {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val result = LocationManagerCompat.isLocationEnabled(locationManager)

            viewModel.isLocationServicesEnabled.postValueIfDifferent(result)

            if (!result) {
                showTurnOnLocationServicesDialog()
            } else {
                if (turnOnLocationServicesDialog.isShowing) {
                    turnOnLocationServicesDialog.dismiss()
                }
            }
        }.logException()
    }

    private fun handleLocationPermissionDialog() {
        if (PermissionManager.hasAccessFineLocationPermission(this)) {
            if (locationPermissionNeededDialog.isShowing) {
                locationPermissionNeededDialog.dismiss()
            }
            viewModel.isLocationPermissionGranted.postValueIfDifferent(true)
        } else {
            PermissionManager.requestRuntimeLocationPermission(this,
                onPermissionGranted = {
                    locationPermissionNeededDialog.dismiss()
                    viewModel.isLocationPermissionGranted.postValueIfDifferent(true)
                },
                onPermissionDenied = { showLocationPermissionErrorDialog() })
        }
    }

    /**
     * Location Permission Dialog
     */
    private fun showLocationPermissionErrorDialog() {
        if (!locationPermissionNeededDialog.isShowing) {
            locationPermissionNeededDialog.show()
        }
    }

    /**
     * Turn On Location Service Dialog
     */
    private fun showTurnOnLocationServicesDialog() {
        if (!turnOnLocationServicesDialog.isShowing) {
            turnOnLocationServicesDialog.show()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            runCatching {
                val permission = Manifest.permission.POST_NOTIFICATIONS
                val hasPermission = checkSelfPermission(permission) ==
                        PackageManager.PERMISSION_GRANTED
                if (!hasPermission) {
                    permissionLauncher.launch(permission)
                }
            }
        }
    }

    private fun setGoogleMapsVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.map.isVisible =
                (destination.id == R.id.homeFragment)
        }
    }

    /**
     * Requests to turn on location services.
     */
    @SuppressLint("MissingPermission")
    fun requestTurnOnLocationServices(
        onResponseSuccess: () -> Unit
    ) {

        val request =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_INTERVAL)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(LOCATION_REQUEST_INTERVAL)
                .build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        builder.setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            onResponseSuccess()
        }

        task.addOnFailureListener { exception ->
            if (exception is ApiException && exception.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                val resolvable = exception as ResolvableApiException
                val intentSenderRequest = IntentSenderRequest.Builder(resolvable.resolution).build()
                resolutionLauncher.launch(intentSenderRequest)
            }
        }
    }

    fun setCurrentLocationAndMoveCamera(location: LatLng) {
        mapManager.moveCameraToLocation(location)
        uiActions?.collapseBottomSheet()
        MapUtils.markerLocation.tryEmit(location)
    }

    fun startTimer(minutes: Int) {
        val durationInMillis = TimeUnit.MINUTES.toMillis(minutes.toLong())

        val data = Data.Builder()
            .putLong("duration", durationInMillis)
            .build()

        val timerWorkRequest = OneTimeWorkRequest.Builder(TimerWorker::class.java)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(timerWorkRequest)
    }

    override fun onResume() {
        super.onResume()
        observeLiveData()
    }

    companion object {
        const val LOCATION_REQUEST_INTERVAL = 60000L
    }
}