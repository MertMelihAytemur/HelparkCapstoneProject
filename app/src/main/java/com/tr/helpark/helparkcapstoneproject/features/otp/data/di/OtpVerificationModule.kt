package com.tr.helpark.helparkcapstoneproject.features.otp.data.di

import com.tr.helpark.helparkcapstoneproject.features.otp.data.OtpVerificationRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.otp.data.remote.OtpVerificationService
import com.tr.helpark.helparkcapstoneproject.features.otp.domain.OtpVerificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class OtpVerificationModule {

    @Provides
    fun provideOtpVerificationService(retrofit: Retrofit): OtpVerificationService {
        return retrofit.create(OtpVerificationService::class.java)
    }

    @Provides
    fun provideOtpVerificationRepository(otpVerificationService: OtpVerificationService): OtpVerificationRepository {
        return OtpVerificationRepositoryImpl(otpVerificationService)
    }
}