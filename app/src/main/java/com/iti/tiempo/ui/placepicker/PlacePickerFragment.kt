package com.iti.tiempo.ui.placepicker

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.base.utils.getDateHomeStyleString
import com.iti.tiempo.base.utils.safeNavigation
import com.iti.tiempo.databinding.FragmentPickPlaceBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.utils.CURRENT_LOCATION
import com.iti.tiempo.utils.MoshiHelper
import com.iti.tiempo.utils.PermissionHandler
import com.iti.tiempo.utils.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*
import javax.inject.Inject

private const val TAG = "PlacePickerFragment"

@AndroidEntryPoint
class PlacePickerFragment :
    BaseFragment<FragmentPickPlaceBinding>(FragmentPickPlaceBinding::inflate), PermissionListener {
    private val permissionHandler: PermissionHandler by lazy {
        PermissionHandler(this, this)
    }
    private var address: String = ""

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var locationRequest: LocationRequest

    @Inject
    lateinit var moshiHelper: MoshiHelper
    var location: LocationDetails = LocationDetails()
    lateinit var mGoogleMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        mGoogleMap = googleMap
        val mapLocation = LatLng(30.0594838, 31.2234448)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLocation, 15.0f))
        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
            Log.i(TAG, "Google Map: $it")
            location.latLng = it
            binding.btnSelect.visibility = View.VISIBLE
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(
                    location.latLng.latitude,
                    location.latLng.longitude,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            } catch (e: IOException) {
                Log.e(TAG, "Geocoder: ", e)
            }
        }
    }

    override fun afterOnCreateView() {
        super.afterOnCreateView()
        permissionHandler.checkForMultiplePermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
//        getCurrentLocation()//ToDO check if unnecessary

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.btnSelect.setOnClickListener {

            if (appSharedPreference.getStringValue(CURRENT_LOCATION, "") == "") {
                navController.safeNavigation(
                    R.id.placePickerFragment,
                    R.id.action_placePickerFragment_to_mainFragment
                )
                appSharedPreference.setValue(
                    CURRENT_LOCATION,
                    moshiHelper.getJsonStringFromObject(LocationDetails::class.java,
                        LocationDetails(location.latLng, address, Date().getDateHomeStyleString()))
                )
            } else
                navigateBack(location)
        }
    }

    private fun navigateBack(location: LocationDetails) = with(navController) {
        location.address =address
        location.lastDate = Date().getDateHomeStyleString()
        previousBackStackEntry?.savedStateHandle?.set(LOCATION_KEY, location)
        navigateUp()
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        location.latLng = LatLng(
                            locationResult.lastLocation.latitude,
                            locationResult.lastLocation.longitude
                        )
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location.latLng, 15.0f))
                        fusedLocationProviderClient.removeLocationUpdates(this)
                        Log.i(TAG, "onLocationResult: $location  ${this@PlacePickerFragment}")
                    }
                }, Looper.getMainLooper()
            )
        }
    }

    override fun shouldShowRationaleInfo() {

    }

    override fun isPermissionGranted(isGranted: Boolean) {
        if (isGranted)
            getCurrentLocation()
    }

    companion object {
        const val LOCATION_KEY = "LOCATION_KEY"
    }
}