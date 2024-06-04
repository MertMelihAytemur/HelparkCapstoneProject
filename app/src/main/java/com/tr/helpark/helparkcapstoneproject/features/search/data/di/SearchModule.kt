package com.tr.helpark.helparkcapstoneproject.features.search.data.di

import com.tr.helpark.helparkcapstoneproject.features.search.data.SearchRepositoryImpl
import com.tr.helpark.helparkcapstoneproject.features.search.data.remote.SearchService
import com.tr.helpark.helparkcapstoneproject.features.search.domain.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class SearchModule {

    @Provides
    fun provideSearchService(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)

    @Provides
    fun provideSearchRepository(searchService: SearchService): SearchRepository =
        SearchRepositoryImpl(searchService)
}