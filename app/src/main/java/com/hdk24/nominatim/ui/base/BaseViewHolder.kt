package com.hdk24.nominatim.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(model: T)
}
