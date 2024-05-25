package com.tr.helpark.helparkcapstoneproject.features.search.data.di

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.tr.helpark.helparkcapstoneproject.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlacesModule {

    @Provides
    @Singleton
    fun providePlacesClient(@ApplicationContext context: Context) : PlacesClient {
        Places.initialize(context, BuildConfig.MAPS_API_KEY)
        return Places.createClient(context)
    }
}