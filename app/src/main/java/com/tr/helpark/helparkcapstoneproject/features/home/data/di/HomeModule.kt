package com.tr.helpark.helparkcapstoneproject.features.home.data.di

import com.tr.helpark.helparkcapstoneproject.features.home.data.HomeRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.home.data.remote.HomeService
import com.tr.helpark.helparkcapstoneproject.features.home.domain.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class HomeModule {

    @Provides
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Provides
    fun provideHomeRepository(homeService: HomeService): HomeRepository =
        HomeRepositoryImpl(homeService)
}