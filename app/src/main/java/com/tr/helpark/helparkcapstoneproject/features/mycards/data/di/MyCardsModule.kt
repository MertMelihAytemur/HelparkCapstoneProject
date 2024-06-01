package com.tr.helpark.helparkcapstoneproject.features.mycards.data.di

import com.tr.helpark.helparkcapstoneproject.features.mycards.data.MyCardsRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.mycards.data.remote.MyCardsService
import com.tr.helpark.helparkcapstoneproject.features.mycards.domain.MyCardsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class MyCardsModule {

    @Provides
    fun provideMyCardsService(retrofit: Retrofit): MyCardsService =
        retrofit.create(MyCardsService::class.java)

    @Provides
    fun provideMyCardsRepository(myCardsService: MyCardsService): MyCardsRepository =
        MyCardsRepositoryImpl(myCardsService)
}