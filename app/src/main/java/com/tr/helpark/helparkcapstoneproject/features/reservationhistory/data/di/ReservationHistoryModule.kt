package com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.di

import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.ReservationHistoryRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.data.remote.ReservationHistoryService
import com.tr.helpark.helparkcapstoneproject.features.reservationhistory.domain.ReservationHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class ReservationHistoryModule {

    @Provides
    fun provideReservationHistoryService(retrofit : Retrofit) : ReservationHistoryService {
        return retrofit.create(ReservationHistoryService::class.java)
    }

    @Provides
    fun provideReservationHistoryRepository(
        reservationHistoryService: ReservationHistoryService
    ) : ReservationHistoryRepository {
        return ReservationHistoryRepositoryImpl(reservationHistoryService)
    }
}