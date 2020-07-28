package com.hdk24.nominatim.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hdk24.nominatim.data.model.Address
import com.hdk24.nominatim.databinding.ViewAddressItemBinding
import com.hdk24.nominatim.ui.base.BaseViewHolder

/*
 *  Created by Hanantadk on 24/07/20.
 *  Copyright (c) 2020. All rights reserved.
 *  Last modified 24/07/20.
 */
class AddressHolder(private val binding: ViewAddressItemBinding) :
    BaseViewHolder<Address>(binding.root) {

    companion object {
        fun create(parent: ViewGroup): BaseViewHolder<Address> {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewAddressItemBinding.inflate(layoutInflater, parent, false)
            return AddressHolder(binding)
        }
    }

    override fun bind(model: Address) {
        binding.data = model
        binding.executePendingBindings()
    }
}