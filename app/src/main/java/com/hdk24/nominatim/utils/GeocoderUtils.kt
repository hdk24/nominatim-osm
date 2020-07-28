package com.hdk24.nominatim.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/*
 *  Created by Hanantadk on 27/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 27/07/20.
 */
class GeocoderUtils(private val context: Context) {

    val geocoder = Geocoder(context, Locale.getDefault())

    fun  reverseGeocode(lat: Double, lng:Double, callback:Callback) : Disposable {
      return Observable.fromCallable { geocoder.getFromLocation(lat, lng, 1)}
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe ({ result->
              callback.success((result[0]))
          },{t: Throwable ->  callback.failure(t) })
    }

    interface Callback {
        fun success(address:Address);

        fun failure(e:Throwable);
    }
}