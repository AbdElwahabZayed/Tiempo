package com.iti.tiempo.repo

import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.iti.tiempo.local.AlarmDao
import com.iti.tiempo.model.Alarm
import com.iti.tiempo.utils.ALARM
import com.iti.tiempo.worker.WeatherWorker
import kotlinx.coroutines.flow.Flow
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "AlarmRepoImpl"

class AlarmRepoImpl @Inject constructor(val alarmDao: AlarmDao, val workManager: WorkManager) :
    AlarmRepo {
    override fun getAllAlarms(): Flow<List<Alarm>> {
        return alarmDao.getAllAlarms()
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        val currentCalendar = Calendar.getInstance().apply { time = Date(System.currentTimeMillis()) }
        val hourAndMinuteCalender = Calendar.getInstance().apply { timeInMillis = alarm.time }
        val startCalendar = Calendar.getInstance().apply { time = Date(alarm.fromDate) }
        currentCalendar.set(Calendar.YEAR,startCalendar[Calendar.YEAR])
        currentCalendar.set(Calendar.MONTH,startCalendar[Calendar.MONTH])
        currentCalendar.set(Calendar.DAY_OF_MONTH,startCalendar[Calendar.DAY_OF_MONTH])
        currentCalendar.set(Calendar.MINUTE,hourAndMinuteCalender[Calendar.MINUTE])
        currentCalendar.set(Calendar.HOUR_OF_DAY,hourAndMinuteCalender[Calendar.HOUR_OF_DAY])
        val delay = currentCalendar.timeInMillis - Calendar.getInstance().apply { time = Date(System.currentTimeMillis()) }.timeInMillis

        val request = OneTimeWorkRequestBuilder<WeatherWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(Data.Builder().apply {
                putString(ALARM, alarm.id)
            }.build())
            .build()

        workManager.enqueueUniqueWork(alarm.id, ExistingWorkPolicy.REPLACE, request)
        alarmDao.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        workManager.cancelUniqueWork(alarm.id)
        alarmDao.deleteAlarm(alarm)
    }

    override suspend fun deleteAlarm(id: String) {
        workManager.cancelUniqueWork(id)
        alarmDao.deleteAlarm(id)
    }

    override suspend fun getAlarm(id: String): Alarm? {
        return alarmDao.getAlarm(id)
    }
}