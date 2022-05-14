package com.iti.tiempo.network

import com.iti.tiempo.model.WeatherResponse
import com.iti.tiempo.utils.WEATHER_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("lang") lang : String = "en",
        @Query("appid") apiId: String = WEATHER_API_KEY
    ):Response<WeatherResponse>
}