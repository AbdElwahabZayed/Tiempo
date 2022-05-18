package com.iti.tiempo.worker

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.iti.tiempo.R
import com.iti.tiempo.di.LocalModule
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.Alarm
import com.iti.tiempo.repo.AlarmRepoImpl
import com.iti.tiempo.utils.ALARM
import com.iti.tiempo.utils.IS_NOTIFICATION_ENABLED
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "WeatherWorker"

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
) :
    CoroutineWorker(
        context, params) {


    override suspend fun doWork(): Result {
        val id = inputData.getString(ALARM)
        val sharedPreference = context.getSharedPreferences(
            context.getString(R.string.app_name) + "1",
            Context.MODE_PRIVATE
        )
        val appSharedPreference = AppSharedPreference(sharedPreference, sharedPreference.edit())
        val isNotificationEnabled =
            appSharedPreference.getBooleanValue(IS_NOTIFICATION_ENABLED, true)
        val db = LocalModule.provideDatabase(context)
        val alarmRepo: AlarmRepoImpl =
            AlarmRepoImpl(LocalModule.alarmDao(db),
                WorkManager.getInstance(context))


        when (val alarm = alarmRepo.getAlarm(id ?: "")) {//TODO
            null -> {
            }
            else -> {
                fireNextAlarm(alarm)
                NotificationHandler.createWeatherNotification(context, alarm, "No msgs for today!")
            }
        }
        db.close()
        return Result.success()
    }

    private fun fireNextAlarm(alarm: Alarm) {
        val currentCalendar =
            Calendar.getInstance().apply { time = Date(System.currentTimeMillis()) }
        val initialCalender = Calendar.getInstance().apply { timeInMillis = alarm.time }
        val startCalendar = Calendar.getInstance().apply { time = Date(alarm.fromDate) }
        currentCalendar.set(Calendar.YEAR, startCalendar[Calendar.YEAR])
        currentCalendar.set(Calendar.MONTH, startCalendar[Calendar.MONTH])
        currentCalendar.set(Calendar.DAY_OF_MONTH, startCalendar[Calendar.DAY_OF_MONTH])
        currentCalendar.set(Calendar.MINUTE, initialCalender[Calendar.MINUTE])
        currentCalendar.set(Calendar.HOUR_OF_DAY, initialCalender[Calendar.HOUR_OF_DAY])
        val delay = currentCalendar.timeInMillis - Calendar.getInstance()
            .apply { time = Date(System.currentTimeMillis()) }.timeInMillis
        if (delay > 0) {
            val request = OneTimeWorkRequestBuilder<WeatherWorker>()
                .setInputData(Data.Builder().putString(ALARM, alarm.id).build())
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(alarm.id, ExistingWorkPolicy.REPLACE, request)
        } else
            Toast.makeText(context, context.getText(R.string.expired_date), Toast.LENGTH_SHORT)
                .show()
    }


}