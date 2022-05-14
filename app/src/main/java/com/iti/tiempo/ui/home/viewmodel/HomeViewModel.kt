package com.iti.tiempo.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.model.WeatherResponse
import com.iti.tiempo.repo.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val weatherRepo: WeatherRepo) : ViewModel() {
    private val _currentWeather: MutableLiveData<WeatherResponse> = MutableLiveData()
    val currentWeather: LiveData<WeatherResponse> = _currentWeather
    fun getCurrentWeather(currentLocation: LocationDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.getWeather(currentLocation, this).onEach {
                _currentWeather.postValue(it)
            }.launchIn(viewModelScope)
        }
    }
}