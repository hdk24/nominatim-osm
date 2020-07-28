package com.hdk24.nominatim.data.mapper

import com.hdk24.nominatim.data.model.Address
import com.hdk24.nominatim.data.remote.OSMResponse

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
object AddressMapper {

    fun mapToModel(source: OSMResponse): Address {

            return Address(
                source.placeId.toString(),
                source.nameDetails.name,
                source.address.city,
                source.address.state,
                source.address.country,
                source.address.postcode,
                source.displayName
            )

    }
}