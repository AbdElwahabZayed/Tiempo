package com.iti.tiempo.ui.alarm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.tiempo.model.Alarm
import com.iti.tiempo.model.WeatherResponse
import com.iti.tiempo.repo.AlarmRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AlarmViewModel"

@HiltViewModel
class AlarmViewModel @Inject constructor(val alarmRepo: AlarmRepo) : ViewModel() {
    private val _alarms: MutableLiveData<List<Alarm>> = MutableLiveData()
    val alarms: LiveData<List<Alarm>> = _alarms
    private val _exceptions: MutableLiveData<Throwable> = MutableLiveData()
    val exceptions: LiveData<Throwable> = _exceptions
    private val exceptionHandler = CoroutineExceptionHandler { _, ex ->
        Log.e(TAG, ": exception ", ex)
        _exceptions.postValue(ex)
    }

    fun getAllAlarms() {
        alarmRepo.getAllAlarms().onEach {
            _alarms.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            alarmRepo.insertAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            alarmRepo.deleteAlarm(alarm)
        }
    }

    fun insertAlarm(id: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            alarmRepo.deleteAlarm(id)
        }
    }

}