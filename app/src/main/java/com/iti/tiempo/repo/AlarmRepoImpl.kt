package com.iti.tiempo.repo

import com.iti.tiempo.local.AlarmDao
import com.iti.tiempo.model.Alarm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmRepoImpl @Inject constructor(val alarmDao:AlarmDao) :AlarmRepo {
    override fun getAllAlarms(): Flow<List<Alarm>> {
        return alarmDao.getAllAlarms()
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        alarmDao.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    override suspend fun deleteAlarm(id: String) {
        alarmDao.deleteAlarm(id)
    }
}