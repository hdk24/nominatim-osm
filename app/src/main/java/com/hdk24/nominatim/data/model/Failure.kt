package com.hdk24.nominatim.data.model

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
data class Failure(val code: Int, val msg: String) : Throwable()