package com.tr.helpark.helparkcapstoneproject.features.favorites.data.di

import com.tr.helpark.helparkcapstoneproject.features.favorites.data.FavoriteRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.favorites.data.remote.FavoriteService
import com.tr.helpark.helparkcapstoneproject.features.favorites.domain.FavoriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class FavoritesModule {

    @Provides
    fun provideFavoriteService(retrofit: Retrofit) : FavoriteService {
        return retrofit.create(FavoriteService::class.java)
    }

    @Provides
    fun provideFavoriteRepository(favoriteService: FavoriteService) : FavoriteRepository {
        return FavoriteRepositoryImpl(favoriteService)
    }
}