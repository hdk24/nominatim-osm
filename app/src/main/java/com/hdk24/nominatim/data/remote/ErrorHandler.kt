package com.hdk24.nominatim.data.remote

import com.hdk24.nominatim.data.model.Failure
import com.hdk24.nominatim.data.remote.StatusCode.UNKNOWN_ERROR
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/*
 *  Created by Hanantadk on 22/03/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 22/03/20.
 */
object ErrorHandler {

    fun getError(throwable: Throwable): Failure {
        return try {
            when {
                isConnectionTimeout(throwable) -> Failure(
                    StatusCode.TIMEOUT,
                    throwable.message ?: ""
                )
                isRequestCanceled(throwable) -> Failure(
                    StatusCode.REQUEST_CANCELED,
                    throwable.message ?: ""
                )
                noInternetAvailable(throwable) -> Failure(
                    StatusCode.NO_CONNECTION,
                    throwable.message ?: ""
                )
                isHttpException(throwable) -> handleErrorOnNext(throwable)
                else -> Failure(UNKNOWN_ERROR, throwable.message ?: "")
            }
        } catch (e: Exception) {
            Failure(UNKNOWN_ERROR, e.message ?: "")
        }
    }

    private fun handleErrorOnNext(e: Throwable): Failure {
        return if (e is HttpException) {
            val responseBody = e.response()?.errorBody()
            Failure(e.code(), "Error API : ${responseBody?.string()}")
        } else {
            Failure(UNKNOWN_ERROR, "Something wrong")
        }
    }

    private fun isRequestCanceled(throwable: Throwable): Boolean {
        return throwable is IOException && throwable.message == "Canceled"
    }

    private fun noInternetAvailable(throwable: Throwable): Boolean {
        return throwable is UnknownHostException
    }

    private fun isConnectionTimeout(throwable: Throwable): Boolean {
        return throwable is SocketTimeoutException
    }

    private fun isHttpException(throwable: Throwable): Boolean {
        return throwable is HttpException
    }

}
