package com.iti.tiempo.local

import androidx.room.*
import com.iti.tiempo.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherResponse: WeatherResponse)

    @Delete
    suspend fun deleteWeather(weatherResponse: WeatherResponse)

    @Query("select * from weatherresponse where lat != :lat AND lon != :lon")
    fun getAllFavorites(lat:Double, lon:Double):Flow<List<WeatherResponse>>

    @Query("select * from weatherresponse where lat = :lat AND lon = :lon")
    fun getWeather(lat:Double, lon:Double):Flow<WeatherResponse>

    @Query("delete from weatherresponse where lat = :lat AND lon = :lon")
    suspend fun deleteWeather(lat:Double, lon:Double)

}