package com.iti.tiempo.ui.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.base.utils.getDateHomeStyleString
import com.iti.tiempo.base.utils.isGPSActive
import com.iti.tiempo.databinding.FragmentHomeBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.DailyItem
import com.iti.tiempo.model.HourlyItem
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.network.exceptions.MyException
import com.iti.tiempo.ui.home.viewmodel.HomeViewModel
import com.iti.tiempo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*
import javax.inject.Inject


private const val TAG = "HomeFragment"
private const val REQUEST_CHECK_SETTINGS = 555

@AndroidEntryPoint
@SuppressLint("NotifyDataSetChanged")
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    PermissionListener, OnClickActionListener {
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
        mAdapter = HomeAdapter(adapterData, appSharedPreference, this)

        binding.rvHome.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
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
            when (currentLocation) {
                null -> {
                    getCurrentLocation()
                }
                else -> {
                    mViewModel.getCurrentWeather(currentLocation!!,
                        appSharedPreference.getStringValue(LOCALE, "en"))
                }
            }

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
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        try {
                            val addresses = geocoder.getFromLocation(
                                locationResult.lastLocation.latitude,
                                locationResult.lastLocation.longitude,
                                1
                            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            val mAddress =
                                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            currentLocation = LocationDetails(LatLng(
                                locationResult.lastLocation.latitude,
                                locationResult.lastLocation.longitude
                            ), mAddress, Date().getDateHomeStyleString())
                            appSharedPreference.setValue(
                                CURRENT_LOCATION,
                                moshiHelper.getJsonStringFromObject(LocationDetails::class.java,
                                   currentLocation!!)
                            )

                        } catch (e: IOException) {
                            Log.e(TAG, "Geocoder: ", e)
                        }

                        mViewModel.getCurrentWeather(currentLocation!!,
                            appSharedPreference.getStringValue(LOCALE, "en"))
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
            if (isGPSActive()) {
                getCurrentLocation()
            } else {
                adapterData.clear()
                adapterData.add(MyException.GPSIsDisabled)
                mAdapter?.notifyDataSetChanged()
            }
        } else {
            adapterData.clear()
            adapterData.add(MyException.NoGPSPermission)
            mAdapter?.notifyDataSetChanged()
        }
    }

    override fun onClickEnable() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)


        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { ex ->
            when (ex) {
                is ResolvableApiException -> {
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ex.startResolutionForResult(requireActivity(),
                            REQUEST_CHECK_SETTINGS)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }
    }

    override fun onClickAllow() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}