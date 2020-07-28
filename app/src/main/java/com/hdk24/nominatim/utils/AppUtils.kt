package com.hdk24.nominatim.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
object AppUtils {

    fun isPermissionGranted(context: Context, permissions: Array<out String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}