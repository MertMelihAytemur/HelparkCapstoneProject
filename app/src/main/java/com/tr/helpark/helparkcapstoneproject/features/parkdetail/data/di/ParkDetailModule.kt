package com.tr.helpark.helparkcapstoneproject.features.parkdetail.data.di

import com.tr.helpark.helparkcapstoneproject.features.parkdetail.data.remote.ParkDetailService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class ParkDetailModule {

    @Provides
    fun provideParkDetailService(retrofit : Retrofit): ParkDetailService {
        return retrofit.create(ParkDetailService::class.java)
    }
}