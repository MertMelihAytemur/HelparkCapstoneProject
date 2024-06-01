package com.tr.helpark.helparkcapstoneproject.features.mycars.data.di

import com.tr.helpark.helparkcapstoneproject.features.mycars.data.MyCarsRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.remote.MyCarsService
import com.tr.helpark.helparkcapstoneproject.features.mycars.domain.MyCarsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class MyCarsModule {

    @Provides
    fun provideCarService(retrofit: Retrofit) : MyCarsService{
        return retrofit.create(MyCarsService::class.java)
    }

    @Provides
    fun provideCarRepository(carService: MyCarsService) : MyCarsRepository {
        return MyCarsRepositoryImpl(carService)
    }
}