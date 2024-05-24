package com.tr.helpark.helparkcapstoneproject.features.main.dialog



import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.core.content.ContextCompat
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.setWidthPercent
import com.tr.helpark.helparkcapstoneproject.databinding.DialogForceUpdateBinding


class ForceUpdateDialog(
    context : Context,
    private val onUpdateClickAction : () -> Unit
) : Dialog(context) {

    private val binding: DialogForceUpdateBinding by lazy {
        DialogForceUpdateBinding.inflate(layoutInflater)
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

    private fun setBackground() {
        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.bg_imageview_radius)
        window?.setBackgroundDrawable(backgroundDrawable)

        setWidthPercent(90)
    }

    private fun initListeners() {

        binding.tvUpdate.setOnClickListener {
            onUpdateClickAction()
        }
    }

    companion object{
        const val TAG = "LocationPermissionNeededDialog"
    }
}