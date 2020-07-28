package com.hdk24.nominatim.ui.viewModel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hdk24.nominatim.ui.base.BaseViewModel
import com.hdk24.nominatim.utils.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

/*
 *  Created by Hanantadk on 28/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 28/07/20.
 */
class LocationViewModel(context: Context) : BaseViewModel() {

    private val geoCoder = Geocoder(context, Locale.getDefault())
    private var _currentLocation = mutableListOf<Address>()
    private val _location = MutableLiveData<List<Address>>()
    val location: LiveData<List<Address>> get() = _location

    private fun updateState(state: List<Address>) {
        this._location.postValue(state)
    }

    fun getLocation(lat: Double, long: Double) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val result = async(context = Dispatchers.IO) {
                    geoCoder.getFromLocation(lat, long, 1)
                }
                _currentLocation = result.await()
            } catch (e: Throwable) {
                AppLogger.d("Hdk result error : ${e.message}")
            }
            updateState(_currentLocation)
        }
    }
}