package com.tr.helpark.helparkcapstoneproject.features.profile.data.di

import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.features.profile.data.GetProfileRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.profile.data.remote.ProfileService
import com.tr.helpark.helparkcapstoneproject.features.profile.domain.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class ProfileModule {

    @Provides
    fun provideProfileService(retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)

    @Provides
    fun provideProfileRepository(
        profileService: ProfileService,
        preferencesManager: PreferencesManager
    ): ProfileRepository =
        GetProfileRepositoryImpl(profileService, preferencesManager)

}