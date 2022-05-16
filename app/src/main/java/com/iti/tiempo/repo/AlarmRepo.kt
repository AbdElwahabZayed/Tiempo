package com.iti.tiempo.repo


import com.iti.tiempo.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepo {

    fun getAllAlarms(): Flow<List<Alarm>>

    suspend fun insertAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun deleteAlarm(id: String)
}