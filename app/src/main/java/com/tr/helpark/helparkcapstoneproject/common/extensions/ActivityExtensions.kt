package com.tr.helpark.helparkcapstoneproject.common.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tr.helpark.helparkcapstoneproject.R

fun AppCompatActivity.findNavController(@IdRes id: Int = R.id.nav_host_fragment): NavController {
    val navHostFragment = supportFragmentManager.findFragmentById(id) as NavHostFragment
    return navHostFragment.navController
}