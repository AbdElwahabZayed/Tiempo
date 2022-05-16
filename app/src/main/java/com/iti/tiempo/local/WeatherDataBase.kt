package com.iti.tiempo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iti.tiempo.model.Alarm
import com.iti.tiempo.model.WeatherResponse

@Database(entities = [ WeatherResponse::class,Alarm::class ], version =1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    abstract fun alarmDao(): AlarmDao
}