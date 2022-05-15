package com.iti.tiempo.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(private val weatherRepo: WeatherRepo) : ViewModel() {
    private val _currentWeather: MutableLiveData<WeatherResponse> = MutableLiveData()
    val currentWeather: LiveData<WeatherResponse> = _currentWeather
    private val exceptionHandler = CoroutineExceptionHandler { _, ex ->
        Log.e(TAG, ": exception ", ex)
    }

    fun getCurrentWeather(currentLocation: LocationDetails,lang:String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            weatherRepo.getWeather(currentLocation, lang).onEach {
                _currentWeather.postValue(it)
            }.launchIn(viewModelScope)
        }
    }
}