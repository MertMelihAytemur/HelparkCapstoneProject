package com.tr.helpark.helparkcapstoneproject.features.main

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.findNavController
import com.tr.helpark.helparkcapstoneproject.common.extensions.getChildAt
import com.tr.helpark.helparkcapstoneproject.common.extensions.logException
import com.tr.helpark.helparkcapstoneproject.common.extensions.openApplicationDetailSettings
import com.tr.helpark.helparkcapstoneproject.common.extensions.postValueIfDifferent
import com.tr.helpark.helparkcapstoneproject.common.extensions.requestTurnOnLocationServices
import com.tr.helpark.helparkcapstoneproject.common.manager.PermissionManager
import com.tr.helpark.helparkcapstoneproject.common.util.SystemBarWindowInsetListener
import com.tr.helpark.helparkcapstoneproject.common.util.UiConstants
import com.tr.helpark.helparkcapstoneproject.core.LoadingDialog
import com.tr.helpark.helparkcapstoneproject.databinding.ActivityMapsBinding
import com.tr.helpark.helparkcapstoneproject.features.home.presentation.HomeFragment
import com.tr.helpark.helparkcapstoneproject.features.login.presentation.LoginFragment
import com.tr.helpark.helparkcapstoneproject.features.main.dialog.ExitApplicationDialog
import com.tr.helpark.helparkcapstoneproject.features.main.dialog.LocationPermissionNeededDialog
import com.tr.helpark.helparkcapstoneproject.features.main.dialog.TurnOnLocationServicesDialog
import com.tr.helpark.helparkcapstoneproject.features.otp.presentation.OtpVerificationFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {

    val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    private lateinit var navController: NavController

    private val binding: ActivityMapsBinding by lazy {
        ActivityMapsBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var locationPermissionNeededDialog: LocationPermissionNeededDialog
    private lateinit var turnOnLocationServicesDialog: TurnOnLocationServicesDialog
    private lateinit var exitApplicationDialog: ExitApplicationDialog

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        // no-op
    }

    /**
     * The livedata that holds status bar height. The fragments
     * can observe to adjust views if necessary.
     */
    private val _statusBarHeightLiveData: MutableLiveData<Int> = MutableLiveData()
    val statusBarHeightLiveData: LiveData<Int> = _statusBarHeightLiveData

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
                            //getCurrentLocationAndMoveCamera()
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
        initOnBackPressedDispatcher()
        observeLiveData()
        registerForGpsReceiver()
        initDialogs()
        askNotificationPermission()
        handleStatusBar()
    }

    private fun initOnBackPressedDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (getCurrentFragment()) {
                    is HomeFragment, is LoginFragment -> moveTaskToBack(true)
                    is OtpVerificationFragment -> findNavController(R.id.nav_host_fragment).popBackStack(
                        R.id.loginFragment,
                        false
                    )

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
            requestTurnOnLocationServices(this,
                onResponseSuccess = { viewModel.isLocationServicesEnabled.value = true },
                onResponseFailed = { handleTurnOnLocationServicesDialog() })
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

    /**
     * Prepares the status bar system view to have 0dp margin.
     */
    private fun handleStatusBar() {
        if (UiConstants.DRAW_UNDER_STATUS_BAR) {
            val listener =
                object : SystemBarWindowInsetListener(true) {
                    override fun onSystemBarHeight(
                        statusBarHeight: Int
                    ) {
                        if (_statusBarHeightLiveData.value != statusBarHeight)
                            _statusBarHeightLiveData.value = statusBarHeight
                    }
                }
            window?.decorView
                ?.setOnApplyWindowInsetsListener(listener)
        } else {
            val listener =
                object : SystemBarWindowInsetListener(false) {
                    override fun onSystemBarHeight(
                        statusBarHeight: Int
                    ) {
                        if (_statusBarHeightLiveData.value != statusBarHeight)
                            _statusBarHeightLiveData.value = statusBarHeight
                    }
                }
            window?.decorView
                ?.getChildAt(0)
                ?.setOnApplyWindowInsetsListener(listener)
        }
    }
}