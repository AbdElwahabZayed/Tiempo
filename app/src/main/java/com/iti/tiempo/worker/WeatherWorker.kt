package com.iti.tiempo.worker

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.iti.tiempo.R
import com.iti.tiempo.base.utils.getLocaleStringResource
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.Alarm
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.model.getAlertDescription
import com.iti.tiempo.model.hasAlerts
import com.iti.tiempo.network.WeatherService
import com.iti.tiempo.repo.AlarmRepo
import com.iti.tiempo.utils.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "WeatherWorker"

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
    val alarmRepo: AlarmRepo,
    val appSharedPreference: AppSharedPreference,
    val weatherService: WeatherService,
    val moshiHelper: MoshiHelper,
) :
    CoroutineWorker(
        context, params) {


    override suspend fun doWork(): Result {
        val id = inputData.getString(ALARM)
        val currentLocation = moshiHelper.getObjFromJsonString(LocationDetails::class.java,
            appSharedPreference.getStringValue(
                CURRENT_LOCATION, ""))
        val isNotificationEnabled =
            appSharedPreference.getBooleanValue(IS_NOTIFICATION_ENABLED, true)
        val alarm = alarmRepo.getAlarm(id ?: "")

        if (isNotificationEnabled)
            when (alarm) {//TODO
                null -> {
                    return Result.success()
                }
                else -> {
                    if (currentLocation != null) {
                        val weatherResponse =
                            weatherService.getWeather(currentLocation.latLng.latitude.toString(),
                                currentLocation.latLng.longitude.toString(),
                                appSharedPreference.getStringValue(
                                    LOCALE, ENGLISH))
                        if (weatherResponse.isSuccessful && weatherResponse.body() != null) {
                            if (weatherResponse.body()!!.hasAlerts()) {
                                NotificationHandler.createWeatherNotification(context,
                                    alarm,
                                    weatherResponse.body()!!.getAlertDescription(context),
                                    appSharedPreference)
                            } else {
                                NotificationHandler.createWeatherNotification(context,
                                    alarm,
                                    context.getLocaleStringResource(appSharedPreference,
                                        R.string.weather_is_fine), appSharedPreference)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Log.e(TAG, "doWork: Error fetching weather")
                                Toast.makeText(context,
                                    context.getLocaleStringResource(appSharedPreference,
                                        R.string.some_thing_went_wrong),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                }
            }
        fireNextAlarm(alarm!!)

//        db.close()
        return Result.success()
    }

    private fun fireNextAlarm(alarm: Alarm) {
        val currentCalendar =
            Calendar.getInstance().apply { time = Date(System.currentTimeMillis()) }
        currentCalendar.add(Calendar.DAY_OF_MONTH, 1)
        currentCalendar.time.before(Date(alarm.toDate))
        val delay = currentCalendar.timeInMillis - Calendar.getInstance()
            .apply { time = Date(System.currentTimeMillis()) }.timeInMillis
        if (delay > 0 && currentCalendar.time.before(Date(alarm.toDate))) {
            val request = OneTimeWorkRequestBuilder<WeatherWorker>()
                .setInputData(Data.Builder().putString(ALARM, alarm.id).build())
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(alarm.id, ExistingWorkPolicy.REPLACE, request)
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, context.getText(R.string.expired_date), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}