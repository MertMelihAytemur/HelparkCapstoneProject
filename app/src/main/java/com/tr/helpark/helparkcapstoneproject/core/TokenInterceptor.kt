package com.tr.helpark.helparkcapstoneproject.core

import com.tr.helpark.helparkcapstoneproject.common.ClientPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val clientPreferences: ClientPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = "ACCESS_TOKEN" //clientPreferences.getToken()

        // If the token is available, add it to the request's headers
        val requestWithAuthorizationHeader = token?.let {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $it")
                .build()
        } ?: originalRequest

        return chain.proceed(requestWithAuthorizationHeader)
    }
}