package com.iti.tiempo.base.utils

import android.content.Context
import android.location.LocationManager
import androidx.fragment.app.Fragment
import kotlin.properties.Delegates

fun Fragment.isGPSActive(): Boolean {
    val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var gpsEnabled by Delegates.notNull<Boolean>()
    try {
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } catch (e: Exception) {
        return false
    }
    return gpsEnabled
}