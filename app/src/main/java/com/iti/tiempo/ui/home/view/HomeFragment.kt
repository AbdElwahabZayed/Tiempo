package com.iti.tiempo.ui.home.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.iti.tiempo.R
import com.iti.tiempo.base.network.DataState
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.databinding.FragmentHomeBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.DailyItem
import com.iti.tiempo.model.HourlyItem
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.network.exceptions.MyException
import com.iti.tiempo.ui.home.viewmodel.HomeViewModel
import com.iti.tiempo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NullPointerException
import javax.inject.Inject

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    PermissionListener {
    private var mAdapter: HomeAdapter? = null
    private var currentLocation: LocationDetails? = null
    private val mNavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private var adapterData: MutableList<Any> = mutableListOf()
    private val permissionHandler: PermissionHandler by lazy {
        PermissionHandler(this, this)
    }
    private val mViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var locationRequest: LocationRequest

    @Inject
    lateinit var moshiHelper: MoshiHelper

    @Inject
    lateinit var appSharedPreference: AppSharedPreference
    override fun afterOnCreateView() {
        super.afterOnCreateView()
        currentLocation =
            moshiHelper.getObjFromJsonString(LocationDetails::class.java,
                appSharedPreference.getStringValue(
                    CURRENT_LOCATION, ""))
        Log.i(TAG, "afterOnCreateView: $currentLocation")
        when (currentLocation) {
            null -> {
                permissionHandler.checkForMultiplePermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                //TODO show item in recycler view for two cases map go to map else can't fetch location
                //Should get location in case of gps when create home fragment
            }
            else -> {
                mViewModel.getCurrentWeather(currentLocation!!, appSharedPreference.getStringValue(
                    LOCALE, "en"))
            }
        }
        setUpView()
    }

    private fun setUpView() {
        binding.swipeLayout.setOnRefreshListener {
            currentLocation =
                moshiHelper.getObjFromJsonString(LocationDetails::class.java,
                    appSharedPreference.getStringValue(
                        CURRENT_LOCATION, ""))
            mViewModel.getCurrentWeather(currentLocation!!,
                appSharedPreference.getStringValue(LOCALE, "en"))
        }
    }

    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        mViewModel.currentWeather.removeObservers(viewLifecycleOwner)
        mViewModel.currentWeather.observe(viewLifecycleOwner) { weather ->
            binding.swipeLayout.isRefreshing = false
            when (weather) {
                null -> {
                    Log.e(TAG, "afterOnViewCreated: ", NullPointerException())
                }
                else -> {
                    adapterData.clear()
                    val locationDetails = LocationDetails(LatLng(weather.lat, weather.lon),
                        weather.address ?: "none",
                        weather.lastDate ?: "none")
                    val weatherStatus = weather.current?.weather!![0]
                    val hours: List<HourlyItem>? = weather.hourly
                    val daily: List<DailyItem>? = weather.daily
                    val current = weather.current
                    adapterData.add(locationDetails)
                    adapterData.add(weatherStatus)
                    adapterData.add(hours!!)
                    adapterData.add(daily!!)
                    adapterData.add(current!!)
                    mAdapter = HomeAdapter(adapterData, appSharedPreference)

                    binding.rvHome.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        adapter = mAdapter
                    }
                }
            }
        }
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
//                        location = LatLng(
//                            locationResult.lastLocation.latitude,
//                            locationResult.lastLocation.longitude
//                        )
                        fusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }, Looper.getMainLooper()
            )
        }
    }


    override fun shouldShowRationaleInfo() {

    }

    override fun isPermissionGranted(isGranted: Boolean) {
        if (isGranted) {
            getCurrentLocation()
        } else {
            adapterData.clear()
            adapterData.add(MyException.NoGPSPermission)
            mAdapter?.notifyDataSetChanged()
        }
    }
}