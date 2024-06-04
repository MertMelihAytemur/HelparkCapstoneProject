package com.tr.helpark.helparkcapstoneproject.features.reservation.data.di

import com.tr.helpark.helparkcapstoneproject.features.reservation.data.ReservationRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.reservation.data.remote.ReservationService
import com.tr.helpark.helparkcapstoneproject.features.reservation.domain.ReservationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class ReservationModule {

    @Provides
    fun provideReservationService(retrofit: Retrofit): ReservationService =
        retrofit.create(ReservationService::class.java)

    @Provides
    fun provideReservationRepository(
        reservationService: ReservationService
    ): ReservationRepository = ReservationRepositoryImpl(reservationService)
}