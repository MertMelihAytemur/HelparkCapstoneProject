package com.tr.helpark.helparkcapstoneproject.features.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.navigateWithAnimation
import com.tr.helpark.helparkcapstoneproject.common.util.Constants
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.tvToolbarTitle.text = getString(R.string.settings_toolbar_title)
        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            cvProfile.setOnClickListener {
                navigateWithAnimation(R.id.action_settingsFragment_to_profileFragment)
            }

            cvSavedCarParks.setOnClickListener {
                //val action = SettingsFragmentDirections.actionSettingsFragmentToSavedCarParksFragment()
                //navigateWithAnimation(action)
            }

            toolbar.icBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cvAboutUs.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVACY_POLICY))
                startActivity(intent)
            }

            cvReservationHistory.setOnClickListener {
                //redirectStore(STORE_URL)
            }
        }
    }
}