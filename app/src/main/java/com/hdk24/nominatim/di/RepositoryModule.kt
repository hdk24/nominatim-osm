package com.hdk24.nominatim.di

import com.hdk24.nominatim.data.repository.AddressRepository
import com.hdk24.nominatim.data.repository.AddressRepositoryImpl
import org.koin.dsl.module

/*
 *  Created by Hanantadk on 27/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 27/07/20.
 */
val repositoryModule = module{

    factory<AddressRepository>{AddressRepositoryImpl(get())}
}