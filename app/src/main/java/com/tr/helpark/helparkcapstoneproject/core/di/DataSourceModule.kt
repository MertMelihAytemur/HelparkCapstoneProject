package com.tr.helpark.helparkcapstoneproject.core.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun providesPreferencesManager(
        @ApplicationContext context: Context,
        gsonProvider: Provider<Gson>
    ): PreferencesManager {
        return PreferencesManager(context, gsonProvider)
    }

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder().create()
}