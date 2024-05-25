package com.tr.helpark.helparkcapstoneproject.features.register.data.di

import com.tr.helpark.helparkcapstoneproject.features.register.data.RegisterRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.register.data.remote.RegisterService
import com.tr.helpark.helparkcapstoneproject.features.register.domain.RegisterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class RegisterModule {

    @Provides
    fun provideRegisterService(retrofit: Retrofit): RegisterService {
        return retrofit.create(RegisterService::class.java)
    }

    @Provides
    fun provideRegisterRepository(registerService: RegisterService): RegisterRepository {
        return RegisterRepositoryImpl(registerService)
    }
}