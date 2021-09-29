package com.appsquare.task.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private val gson: Gson by lazy {
    GsonBuilder()
        .setLenient()
        .create()
}
private val httpClient: OkHttpClient.Builder by lazy {
    OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request: Request =
                chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json").build()
            chain.proceed(request)
        }
}


fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
    .callFactory(OkHttpClient.Builder().build())
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .client(httpClient.build())
    .build()
