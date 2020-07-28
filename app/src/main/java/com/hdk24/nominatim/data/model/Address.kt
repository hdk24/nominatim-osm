package com.hdk24.nominatim.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
@Parcelize
data class Address(
    val id:String?="",
    var name: String? = "",
    var city: String? = "",
    var state: String? = "",
    var country: String? = "",
    var postalCode: String? = "",
    var formatAddress:String?=""
) : Parcelable
