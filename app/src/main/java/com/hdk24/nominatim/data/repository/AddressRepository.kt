package com.hdk24.nominatim.data.repository

import com.hdk24.nominatim.data.model.Address
import io.reactivex.Single

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
interface AddressRepository {

    suspend fun getAddressSuggestion(query: String): List<Address>
}