package com.tr.helpark.helparkcapstoneproject.common.manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

object PermissionManager {

    fun requestRuntimeLocationPermission(
        context: Context,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        Dexter.withContext(context)
            .withPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(
                object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        onPermissionGranted()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        onPermissionDenied()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }
            )
            .withErrorListener {
                onPermissionDenied()
            }
            .onSameThread()
            .check()
    }

    fun hasAccessFineLocationPermission(activity: Activity) : Boolean{
        val permissionRequestAccessFineLocation = ContextCompat.checkSelfPermission(activity,
            Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionRequestAccessFineLocation  == PackageManager.PERMISSION_GRANTED
    }

}