package com.iti.tiempo.repo

import com.google.android.gms.maps.model.LatLng
import com.iti.tiempo.base.network.DataState
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.model.WeatherResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    suspend fun getWeather(
        currentLocation: LocationDetails,
        lang: String = "en",
    ): Flow<WeatherResponse>

    suspend fun getAllFavoritesWeather(currentLocation: LatLng): Flow<List<WeatherResponse>>
    suspend fun deleteWeather(weatherResponse: WeatherResponse)
    suspend fun deleteWeather(lat:Double,lon :Double)
    suspend fun insertWeather(
        currentLocation: LocationDetails,
        lang: String,
    )
}