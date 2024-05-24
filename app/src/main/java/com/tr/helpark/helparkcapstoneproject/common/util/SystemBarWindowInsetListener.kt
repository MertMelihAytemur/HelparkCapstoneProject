package com.tr.helpark.helparkcapstoneproject.common.util

import android.graphics.Rect
import android.view.View
import android.view.WindowInsets
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat

open class SystemBarWindowInsetListener(
    private val drawUnderNavigationBar: Boolean
): View.OnApplyWindowInsetsListener {

    /**
     * The status bar height, holds the maximum value.
     */
    private var statusBarHeight = 0

    /**
     * The navigation bar height, holds the maximum value.
     */
    private var navigationBarHeight = 0

    /**
     * The optional function to override.
     */
    open fun onSystemBarHeight(statusBarHeight: Int) {

    }

    /**
     * The window inset manager.
     */
    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
        val insetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets)
        val statusBarHeightLocal = insetsCompat
            .getInsets(WindowInsetsCompat.Type.statusBars())
            .top

        var isChanged = false
        if (statusBarHeight < statusBarHeightLocal) {
            statusBarHeight = statusBarHeightLocal
            isChanged = true
        }

        if (isChanged) {
            onSystemBarHeight(statusBarHeight)
        }

        val consumedInsets: WindowInsets = if (drawUnderNavigationBar) {
            val newStatusBarInsets = Insets.of(Rect())

            WindowInsetsCompat.Builder(insetsCompat)
                .setInsets(WindowInsetsCompat.Type.statusBars(), newStatusBarInsets)
                .build()
                .toWindowInsets()!!
        } else {
            WindowInsetsCompat.Builder(insetsCompat)
                .setInsets(WindowInsetsCompat.Type.statusBars(), Insets.of(Rect()))
                .build()
                .toWindowInsets()!!
        }
        return consumedInsets
    }
}