package com.hdk24.nominatim.di

import com.hdk24.nominatim.ui.viewModel.AddressViewModel
import com.hdk24.nominatim.ui.viewModel.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
val viewModelModule = module {

    viewModel { AddressViewModel(get()) }

    viewModel { LocationViewModel(get()) }
}