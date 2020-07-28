package com.hdk24.nominatim

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.hdk24.nominatim.di.networkModule
import com.hdk24.nominatim.di.repositoryModule
import com.hdk24.nominatim.di.viewModelModule
import com.hdk24.nominatim.utils.AppLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        AppLogger.init()
        startKoin {
            androidContext(this@App)
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}
