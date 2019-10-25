package com.rocket.retrofitwithrxjavatutorial

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class Repository (
    baseUrl: String,
    isDebugEnabled: Boolean,
    apiKey: String
){

    private val apiKeyHeader: String = "x-api-key"
    val retrofit: Retrofit

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (isDebugEnabled) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder().addInterceptor{
            chain ->
            val request = chain.request().newBuilder()
                .addHeader(apiKeyHeader, apiKey)
                .build()
            chain.proceed(request)
        }.addInterceptor(loggingInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}