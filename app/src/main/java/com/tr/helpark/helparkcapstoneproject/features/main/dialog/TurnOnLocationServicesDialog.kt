package com.tr.helpark.helparkcapstoneproject.features.main.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.core.content.ContextCompat
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.DialogTurnOnLocationServicesBinding

/**
 *Created by Mert Melih Aytemur on 2/25/2023.
 */
class TurnOnLocationServicesDialog(
    context : Context,
    private val onOkClickListener: () -> Unit
    ): Dialog(context) {

    private val binding: DialogTurnOnLocationServicesBinding by lazy {
        DialogTurnOnLocationServicesBinding.inflate(layoutInflater)
    }
    private fun setupUi() {
        setBackground()
        initListeners()
    }
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        setCancelable(false)
        setupUi()
    }
    private fun initListeners() {
        binding.btnLocationService.setOnClickListener {
            onOkClickListener()
        }
    }

    private fun setBackground() {
        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.bg_imageview_radius)
        window?.setBackgroundDrawable(backgroundDrawable)
    }

    companion object{
        const val TAG = "TurnOnLocationServicesDialog"
    }
}