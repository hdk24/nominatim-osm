package com.hdk24.nominatim.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
data class OSMResponse(
    @Expose
    @SerializedName("place_id")
    val placeId: Long,

    @Expose
    @SerializedName("lat")
    val lat: Double,

    @Expose
    @SerializedName("lon")
    val long: Double,

    @Expose
    @SerializedName("display_name")
    val displayName: String,

    @Expose
    @SerializedName("address")
    val address: AddressDetail,

    @Expose
    @SerializedName("namedetails")
    val nameDetails: NameDetail
)

data class AddressDetail(
    @Expose
    @SerializedName("road")
    val road: String? = "",

    @Expose
    @SerializedName("city_district")
    val cityDistrict: String? = "",

    @Expose
    @SerializedName("county")
    val county: String? = "",

    @Expose
    @SerializedName("city")
    val city: String? = "",

    @Expose
    @SerializedName("state")
    val state: String? = "",

    @Expose
    @SerializedName("postcode")
    val postcode: String? = "",

    @Expose
    @SerializedName("country")
    val country: String? = ""
)

data class NameDetail(
    @Expose
    @SerializedName("name")
    val name: String? = ""
)
