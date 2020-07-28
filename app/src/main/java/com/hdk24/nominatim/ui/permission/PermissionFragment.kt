package com.hdk24.nominatim.ui.permission

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.hdk24.nominatim.R
import com.hdk24.nominatim.databinding.FragmentPermissionBinding
import com.hdk24.nominatim.ui.MainActivity
import com.hdk24.nominatim.ui.base.BaseFragment
import com.hdk24.nominatim.utils.AppConstants.LOCATION_REQUEST
import com.hdk24.nominatim.utils.AppLogger
import com.hdk24.nominatim.utils.AppUtils

class PermissionFragment : BaseFragment<FragmentPermissionBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_permission

    override fun onViewReady(savedInstance: Bundle?) {
        binding.btnActivate.setOnClickListener {
            requestPermissions(MainActivity.permission, LOCATION_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST) {
            if (AppUtils.isPermissionGranted(requireContext(), MainActivity.permission)) {
                goToAddress()
            }
        }
    }

    private fun goToAddress() {
        findNavController().navigate(R.id.action_permissionFragment_to_addressFragment)
    }
}
