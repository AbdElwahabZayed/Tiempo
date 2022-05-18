package com.iti.tiempo.ui.favorites.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.model.WeatherResponse
import com.iti.tiempo.repo.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FavoritesViewModel"

@HiltViewModel
class FavoritesViewModel @Inject constructor(val weatherRepo: WeatherRepo) : ViewModel() {
    private val _favoritesWeathers: MutableLiveData<List<WeatherResponse>> = MutableLiveData()
    val favoritesWeathers: LiveData<List<WeatherResponse>> = _favoritesWeathers
    private val _exceptions: MutableLiveData<Throwable> = MutableLiveData()
    val exceptions: LiveData<Throwable> = _exceptions
    private val exceptionHandler = CoroutineExceptionHandler { _, ex ->
        Log.e(TAG, ": exception ", ex)
        _exceptions.postValue(ex)
    }
    private val _currentWeather: MutableLiveData<WeatherResponse> = MutableLiveData()
    val currentWeather: LiveData<WeatherResponse> = _currentWeather



    fun getFavoritesWeather(currentLocation: LocationDetails) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            weatherRepo.getAllFavoritesWeather(currentLocation.latLng).onEach {
                _favoritesWeathers.postValue(it)
            }.launchIn(viewModelScope)
        }
    }
    fun insertIntoFavorites(currentLocation: LocationDetails,lang:String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            weatherRepo.insertWeather(currentLocation,lang)
        }
    }

    fun deleteWeather(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            weatherRepo.deleteWeather(latLng.latitude, latLng.longitude)
        }
    }
}