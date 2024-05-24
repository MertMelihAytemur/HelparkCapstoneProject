package com.tr.helpark.helparkcapstoneproject.features.main.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.core.content.ContextCompat
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.databinding.DialogExitApplicationBinding


/**
 *Created by Mert Melih Aytemur on 2/26/2023.
 */
class ExitApplicationDialog(
    context: Context,
    private val onExitClickAction: () -> Unit
) : Dialog(context) {

    private val binding: DialogExitApplicationBinding by lazy {
        DialogExitApplicationBinding.inflate(layoutInflater)
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setupUi()
    }

    private fun setupUi() {
        setBackground()
        initListeners()
    }

    private fun setBackground() {
        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.bg_imageview_radius)
        window?.setBackgroundDrawable(backgroundDrawable)
    }


    private fun initListeners() {
        binding.btnExit.setOnClickListener {
            onExitClickAction()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "ExitApplicationDialog"
    }
}