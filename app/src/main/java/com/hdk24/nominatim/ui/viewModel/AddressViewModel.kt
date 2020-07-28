package com.hdk24.nominatim.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hdk24.nominatim.data.model.Address
import com.hdk24.nominatim.data.remote.ErrorHandler
import com.hdk24.nominatim.data.repository.AddressRepository
import com.hdk24.nominatim.data.state.ResultState
import com.hdk24.nominatim.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
class AddressViewModel(private val repository: AddressRepository) : BaseViewModel() {

    private val _address = MutableLiveData<ResultState<List<Address>>>()
    val address: LiveData<ResultState<List<Address>>> get() = _address


    fun doSearch(query: String) {
        viewModelScope.launch(Main) {
            try {
                updateState(ResultState.OnLoading())
                val result = async(context = IO) {
                    repository.getAddressSuggestion(query)
                }
                updateState(ResultState.OnSuccess(result.await()))
            } catch (e: Throwable) {
                updateState(ResultState.OnError(ErrorHandler.getError(e)))
            }
        }
    }

    private fun updateState(state: ResultState<List<Address>>) {
        this._address.postValue(state)
    }
}