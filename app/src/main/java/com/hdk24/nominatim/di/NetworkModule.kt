package com.hdk24.nominatim.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hdk24.nominatim.BuildConfig
import com.hdk24.nominatim.data.remote.ApiService
import com.hdk24.nominatim.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
val networkModule = module {

    // inject Gson
    single<Gson> { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }

    // inject okHttp
    single<OkHttpClient> {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor {
                val requestBuilder = it.request().newBuilder()
                requestBuilder.addHeader("Connection", "close")
                requestBuilder.addHeader("user-agent", AppConstants.USER_AGENT)
                it.proceed(requestBuilder.build())
            }
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    // inject retrofit
    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .client(get())
            .build()
    }

    // inject api service
    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}