package com.tr.helpark.helparkcapstoneproject.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.findNavController
import com.tr.helpark.helparkcapstoneproject.features.home.presentation.HomeFragment
import com.tr.helpark.helparkcapstoneproject.features.login.presentation.LoginFragment
import com.tr.helpark.helparkcapstoneproject.features.otp.presentation.OtpVerificationFragment

class MapsActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController()
        initOnBackPressedDispatcher()
    }

    private fun initOnBackPressedDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(getCurrentFragment()) {
                    is HomeFragment, is LoginFragment -> moveTaskToBack(true)
                    is OtpVerificationFragment -> findNavController(R.id.nav_host_fragment).popBackStack(R.id.loginFragment, false)
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
}