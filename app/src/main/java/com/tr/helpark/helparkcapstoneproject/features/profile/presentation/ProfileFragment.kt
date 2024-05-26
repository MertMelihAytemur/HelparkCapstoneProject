package com.tr.helpark.helparkcapstoneproject.features.profile.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.helpark.helpark.common.utils.preferences.PreferencesKeys.KEY_USER_PROFILE
import com.tr.helpark.helparkcapstoneproject.common.extensions.toPhoneNumberFormat
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.base.BaseFragment
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentProfileBinding
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.uimodel.GetProfileUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {
    override val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private var profile: GetProfileUiModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            setProfile()
        }

        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            toolbar.icBack.setOnClickListener {
                findNavController().popBackStack()
            }

            toolbar.tvToolbarTitle.text = "Profil Bilgilerim"
        }
    }

    private suspend fun setProfile() {
        lifecycleScope.launch {
            profile = preferencesManager.getModel(KEY_USER_PROFILE, typeOf<GetProfileUiModel>())

            profile?.let {
                with(binding) {
                    "${it.name} ${it.surname}".also { tvName.text = it }
                    tvEmail.text = it.email
                    tvPhoneNumber.text = it.phoneNumber?.toPhoneNumberFormat()
                }
            }
        }.join()
    }
}