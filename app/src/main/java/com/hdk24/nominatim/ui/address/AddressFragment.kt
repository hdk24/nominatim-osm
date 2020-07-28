package com.hdk24.nominatim.ui.address

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.hdk24.nominatim.R
import com.hdk24.nominatim.data.state.ResultState
import com.hdk24.nominatim.databinding.FragmentAddressBinding
import com.hdk24.nominatim.ui.MainActivity
import com.hdk24.nominatim.ui.adapter.AddressAdapter
import com.hdk24.nominatim.ui.base.BaseFragment
import com.hdk24.nominatim.ui.helper.DebounceQueryTextListener
import com.hdk24.nominatim.ui.viewModel.AddressViewModel
import com.hdk24.nominatim.ui.viewModel.LocationViewModel
import com.hdk24.nominatim.utils.AppLogger
import com.hdk24.nominatim.utils.AppUtils
import com.hdk24.nominatim.utils.NetworkUtils
import org.koin.android.ext.android.inject


// current location :  https://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
// illustration : https://icons8.com/illustrations/style--pablo-1
class AddressFragment : BaseFragment<FragmentAddressBinding>() {

    // location updates interval - 10sec
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

    private val REQUEST_CHECK_SETTINGS = 100

    // bunch of location related apis
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mSettingsClient: SettingsClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest
    private lateinit var mLocationCallback: LocationCallback
    private var mCurrentLocation: Location? = null
    private var mRequestingLocationUpdates: Boolean = false

    private val viewModel: AddressViewModel by inject()
    private val locationViewModel: LocationViewModel by inject()
    private val mAdapter = AddressAdapter()

    override fun getLayoutId(): Int = R.layout.fragment_address

    override fun onViewReady(savedInstance: Bundle?) {
        initViews()
        initLocation()
        subscribeLocation()
    }

    private fun initViews() {
        binding.searchView.setOnQueryTextListener(
            DebounceQueryTextListener(this@AddressFragment.lifecycle, 1000) {
                if (it.length > 3) {
                    viewModel.doSearch(it)
                } else {
                    binding.contentCurrentLocation.visibility = View.VISIBLE
                    binding.rvAddress.visibility = View.GONE
                    mAdapter.submitList(emptyList())
                }
            }
        )

        binding.rvAddress.layoutManager = LinearLayoutManager(requireContext())
        viewModel.address.observe(this, Observer { state ->
            when (state) {
                is ResultState.OnLoading -> {
                    binding.contentCurrentLocation.visibility = View.GONE
                }
                is ResultState.OnError -> {
                    binding.contentCurrentLocation.visibility = View.VISIBLE
                }
                is ResultState.OnSuccess -> {
                    binding.rvAddress.visibility = View.VISIBLE
                    binding.rvAddress.adapter = mAdapter
                    mAdapter.submitList(state.data)
                }
            }
        })
    }

    /**
     * update location when network is connected.
     * get location by latitude and longitude in background task
     */
    private fun updateLocation() {
        AppLogger.d("Hdk => Lat: " + mCurrentLocation?.latitude + ", " + "Lng: " + mCurrentLocation?.longitude)
        if (NetworkUtils.isConnected(requireContext())) {
            locationViewModel.getLocation(
                mCurrentLocation?.latitude ?: 0.0,
                mCurrentLocation?.longitude ?: 0.0
            )
        }
    }

    /**
     * get reverse geocoder location
     */
    private fun subscribeLocation() {
        locationViewModel.location.observe(this, Observer { location ->
            location.forEach {
                val address = it.getAddressLine(0)
                val city = it.locality
                val state = it.adminArea
                val country = it.countryName
                val postalCode = it.postalCode
                val knownName = it.featureName
                binding.tvCurrentAddress.text = address
                AppLogger.d("Hdk => Location : $address")
            }

            if(mAdapter.itemCount>0 || location.isEmpty()){
                binding.contentCurrentLocation.visibility = View.GONE
            } else {
                binding.contentCurrentLocation.visibility = View.VISIBLE
            }
        })
    }

    /**
     * set up for current location
     */
    private fun initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mSettingsClient = LocationServices.getSettingsClient(requireActivity())
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // location is received
                mCurrentLocation = locationResult.lastLocation
                updateLocation()
            }
        }

        mLocationRequest = LocationRequest()
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()
        mRequestingLocationUpdates = true
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mSettingsClient
            .checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(requireActivity()) {
                AppLogger.d("Hdk => All location settings are satisfied.")
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.myLooper()
                )
                updateLocation()
            }
            .addOnFailureListener {
                when ((it as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        AppLogger.d("Hdk => Location settings are not satisfied. Attempting to upgrade location settings ")
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = it as ResolvableApiException
                            startIntentSenderForResult(
                                rae.resolution.intentSender,
                                REQUEST_CHECK_SETTINGS,
                                null,
                                0,
                                0,
                                0,
                                null
                            )
                        } catch (sie: SendIntentException) {
                            AppLogger.d("Hdk => PendingIntent unable to execute request.")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                        AppLogger.d("Hdk => ERROR :  $errorMessage")
                    }
                }

                updateLocation()
            }
    }

    /**
     * stop getting update location
     */
    private fun stopLocationUpdates() {
        mRequestingLocationUpdates = false
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener {
                AppLogger.d("Hdk => REMOVE COMPLETED")
                mRequestingLocationUpdates = true
            }
    }

    /**
     * Resuming location updates depending on button state and
     * allowed permissions
     */
    override fun onResume() {
        super.onResume()
        AppLogger.d("Hdk => OnResume")
        if (mRequestingLocationUpdates && AppUtils.isPermissionGranted(
                requireContext(),
                MainActivity.permission
            )
        ) {
            startLocationUpdates()
        } else {
            AppLogger.d("Hdk => Open Bottom Sheet")
            //startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    /**
     * stop location when app on pause
     */
    override fun onPause() {
        super.onPause()
        if (mRequestingLocationUpdates) stopLocationUpdates()
    }

    /**
     * getting status gps when trigger from dialog
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    AppLogger.d("Hdk => User agreed to make required location settings changes.")
                    mRequestingLocationUpdates = true
                }
                Activity.RESULT_CANCELED -> {
                    AppLogger.d("Hdk => User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                }
            }
        }
    }
}
