package com.hdk24.nominatim.data.repository

import com.hdk24.nominatim.data.mapper.AddressMapper
import com.hdk24.nominatim.data.model.Address
import com.hdk24.nominatim.data.remote.ApiService

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
class AddressRepositoryImpl(private val service: ApiService) : AddressRepository {

    override suspend fun getAddressSuggestion(query: String): List<Address> {
        return service.getSuggestion(query).asSequence()
            .map {
                AddressMapper.mapToModel(it)
            }.toList()
    }
}