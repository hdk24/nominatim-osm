package com.hdk24.nominatim.utils

import android.os.Build
import com.hdk24.nominatim.BuildConfig

/*
 *  Created by Hanantadk on 22/03/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 22/03/20.
 */
object AppConstants {

    val USER_AGENT =
        "android / " + Build.VERSION.SDK_INT + " / " +
                Build.VERSION.RELEASE + " / " +
                BuildConfig.VERSION_NAME + " / " +
                Build.MODEL + " / " +
                Build.MANUFACTURER + " / " +
                Build.BRAND

    val LOCATION_REQUEST = 1000

    val GPS_REQUEST = 1001
}
