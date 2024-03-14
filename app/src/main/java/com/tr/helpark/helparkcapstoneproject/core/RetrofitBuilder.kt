package com.tr.helpark.helparkcapstoneproject.core

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

class RetrofitBuilder() {

    private val builder: Retrofit.Builder = Retrofit.Builder()

    fun baseUrl(baseUrl: String): RetrofitBuilder {
        builder.baseUrl(baseUrl)
        return this
    }

    fun addConverterFactory(factory: Converter.Factory): RetrofitBuilder {
        builder.addConverterFactory(factory)
        return this
    }

    fun client(okHttpClient: OkHttpClient): RetrofitBuilder {
        builder.client(okHttpClient)
        return this
    }

    fun build(): Retrofit {
        return builder.build()
    }

}