package com.tr.helpark.helparkcapstoneproject.features.login.data.di

import com.tr.helpark.helparkcapstoneproject.features.login.data.LoginRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.login.data.remote.LoginService
import com.tr.helpark.helparkcapstoneproject.features.login.domain.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class LoginModule {

    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    fun provideLoginRepository(loginService: LoginService): LoginRepository =
        LoginRepositoryImpl(loginService)
}