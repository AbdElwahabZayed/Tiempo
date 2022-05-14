package com.iti.tiempo.repo

import com.google.android.gms.maps.model.LatLng
import com.iti.tiempo.local.WeatherDao
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.model.WeatherResponse
import com.iti.tiempo.network.WeatherService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WeatherRepoImpl(
    private val weatherService: WeatherService,
    private val weatherDao: WeatherDao,
) : WeatherRepo {


    override suspend fun getWeather(
        currentLocation: LocationDetails,
        scope: CoroutineScope,
        lang: String,
    ): Flow<WeatherResponse> {
        scope.launch {
            val weatherResponse = weatherService.getWeather(
                "${currentLocation.latLng.latitude}",
                "${currentLocation.latLng.longitude}",
                lang
            )
            weatherResponse.body()?.let {
                it.address = currentLocation.address
                it.lastDate = currentLocation.lastDate
                it.lat = currentLocation.latLng.latitude
                it.lon = currentLocation.latLng.longitude
                it.current?.weather!![0].temp = it.current?.temp
                weatherDao.insertWeather(it)
            }
        }
        return weatherDao.getWeather(currentLocation.latLng.latitude,
            currentLocation.latLng.longitude)
    }


    override suspend fun getAllFavoritesWeather(currentLocation: LatLng): Flow<List<WeatherResponse>> {
        return weatherDao.getAllFavorites(currentLocation.latitude, currentLocation.longitude)
    }

    override suspend fun deleteWeather(weatherResponse: WeatherResponse) {
        weatherDao.deleteWeather(weatherResponse)
    }

    override suspend fun insertFavoriteWeather(weatherResponse: WeatherResponse) {
        weatherDao.insertWeather(weatherResponse)
    }

}