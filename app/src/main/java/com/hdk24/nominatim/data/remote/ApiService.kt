package com.hdk24.nominatim.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
interface ApiService {

    @GET("search")
    suspend fun getSuggestion(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: String = "1",
        @Query("namedetails") nameDetails: String = "1"
    ): List<OSMResponse>
}
