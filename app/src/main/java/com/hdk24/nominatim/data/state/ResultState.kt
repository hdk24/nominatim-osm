package com.hdk24.nominatim.data.state

import com.hdk24.nominatim.data.model.Failure

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
sealed class ResultState<out T> {
    class OnLoading<out T> : ResultState<T>()
    data class OnSuccess<out T> (val data: T) : ResultState<T>()
    data class OnError<out T> (val error: Failure) : ResultState<T>()
}