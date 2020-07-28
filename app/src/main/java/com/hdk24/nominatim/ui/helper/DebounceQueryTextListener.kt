package com.hdk24.nominatim.ui.helper

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
 *  Created by Hanantadk on 28/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 28/07/20.
 */

// source : https://android.jlelse.eu/implementing-search-on-type-in-android-with-coroutines-ab117c8f13a4
class DebounceQueryTextListener(
    lifecycle: Lifecycle, private val period:Long, private val callback: (String) -> Unit
) : SearchView.OnQueryTextListener {

    private val coroutineScope = lifecycle.coroutineScope

    private var searchJob: Job? = null

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            newText?.let {
                delay(period)
                callback(newText)
            }
        }
        return false
    }
}