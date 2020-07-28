package com.hdk24.nominatim.ui

import android.Manifest
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.hdk24.nominatim.R
import com.hdk24.nominatim.databinding.ActivityMainBinding
import com.hdk24.nominatim.ui.base.BaseActivity
import com.hdk24.nominatim.utils.AppUtils

class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        val permission = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onViewReady(savedInstance: Bundle?) {
        initNavigationGraph()
    }

    private fun initNavigationGraph() {
        val navHost = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        val navController = navHost.findNavController()
        val graph = navController.navInflater.inflate(R.navigation.navigation)
        if (AppUtils.isPermissionGranted(this, permission)) {
            graph.startDestination = R.id.addressFragment
        } else {
            graph.startDestination = R.id.permissionFragment
        }
        navController.graph = graph
    }
}
