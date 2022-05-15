package com.iti.tiempo.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Entity(primaryKeys = ["lat", "lon"])
data class WeatherResponse(
    @Ignore
    @Json(name = "alerts")
    val alerts: List<AlertsItem>?,

    @Json(name = "current")
    var current: Current?,

    @Json(name = "timezone")
    var timezone: String?,

    @Json(name = "timezone_offset")
    var timezoneOffset: Int?,

    @Json(name = "daily")
    var daily: List<DailyItem>?,
    @NonNull
    @Json(name = "lon")
    var lon: Double = 0.0,

    @Json(name = "hourly")
    var hourly: List<HourlyItem>?,

    @Json(name = "minutely")
    var minutely: List<MinutelyItem>?,
    @NonNull
    @Json(name = "lat")
    var lat: Double = 0.0,

    var address: String?,

    var lastDate: String?,
) {
    constructor() : this(null, null, null, null, null, 0.0, null, null, 0.0, null, null)
}

@JsonClass(generateAdapter = true)
data class WeatherItem(

    @Json(name = "icon")
    val icon: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "main")
    val main: String,

    @Json(name = "id")
    val id: Int,

    var temp: Double?,
)

@JsonClass(generateAdapter = true)
data class Temp(

    @Json(name = "min")
    val min: Double,

    @Json(name = "max")
    val max: Double,

    @Json(name = "eve")
    val eve: Double,

    @Json(name = "night")
    val night: Double,

    @Json(name = "day")
    val day: Double,

    @Json(name = "morn")
    val morn: Double,
)

@JsonClass(generateAdapter = true)
data class AlertsItem(

    @Json(name = "start")
    val start: Int,

    @Json(name = "description")
    val description: String,

    @Json(name = "sender_name")
    val senderName: String,

    @Json(name = "end")
    val end: Int,

    @Json(name = "event")
    val event: String,

    @Json(name = "tags")
    val tags: List<String>,
)

@JsonClass(generateAdapter = true)
data class HourlyItem(

    @Json(name = "temp")
    val temp: Double,

    @Json(name = "visibility")
    val visibility: Int,

    @Json(name = "uvi")
    val uvi: Double,

    @Json(name = "pressure")
    val pressure: Int,

    @Json(name = "clouds")
    val clouds: Int,

    @Json(name = "feels_like")
    val feelsLike: Double,

    @Json(name = "wind_gust")
    val wind_gust: Double?,

    @Json(name = "dt")
    val dt: Long,

    @Json(name = "pop")
    val pop: Double,

    @Json(name = "wind_deg")
    val wind_deg: Int,

    @Json(name = "dew_point")
    val dewPoint: Double,

    @Json(name = "weather")
    val weather: List<WeatherItem>,

    @Json(name = "humidity")
    val humidity: Int,

    @Json(name = "wind_speed")
    val windSpeed: Double,
)

@JsonClass(generateAdapter = true)
data class DailyItem(

    @Json(name = "moonset")
    val moonset: Int,

    @Json(name = "rain")
    val rain: Double?,

    @Json(name = "sunrise")
    val sunrise: Int,

    @Json(name = "temp")
    val temp: Temp,

    @Json(name = "moon_phase")
    val moonPhase: Double,

    @Json(name = "uvi")
    val uvi: Double,

    @Json(name = "moonrise")
    val moonrise: Int,

    @Json(name = "pressure")
    val pressure: Int,

    @Json(name = "clouds")
    val clouds: Int,

    @Json(name = "feels_like")
    val feelsLike: FeelsLike,

    @Json(name = "wind_gust")
    val windGust: Double?,

    @Json(name = "dt")
    val dt: Long,

    @Json(name = "pop")
    val pop: Double,

    @Json(name = "wind_deg")
    val windDeg: Int,

    @Json(name = "dew_point")
    val dewPoint: Double,

    @Json(name = "sunset")
    val sunset: Int,

    @Json(name = "weather")
    val weather: List<WeatherItem>,

    @Json(name = "humidity")
    val humidity: Int,

    @Json(name = "wind_speed")
    val windSpeed: Double,
)

@JsonClass(generateAdapter = true)
data class Current(

    @Json(name = "sunrise")
    val sunrise: Int,

    @Json(name = "temp")
    val temp: Double,

    @Json(name = "visibility")
    val visibility: Int,

    @Json(name = "uvi")
    val uvi: Double,

    @Json(name = "pressure")
    val pressure: Int,

    @Json(name = "clouds")
    val clouds: Int,

    @Json(name = "feels_like")
    val feelsLike: Double,

    @Json(name = "wind_gust")
    val windGust: Double?,

    @Json(name = "dt")
    val dt: Int,

    @Json(name = "wind_deg")
    val windDeg: Int,

    @Json(name = "dew_point")
    val dewPoint: Double,

    @Json(name = "sunset")
    val sunset: Int,

    @Json(name = "weather")
    val weather: List<WeatherItem>,

    @Json(name = "humidity")
    val humidity: Int,

    @Json(name = "wind_speed")
    val windSpeed: Double,
)

@JsonClass(generateAdapter = true)
data class FeelsLike(

    @Json(name = "eve")
    val eve: Double,

    @Json(name = "night")
    val night: Double,

    @Json(name = "day")
    val day: Double,

    @Json(name = "morn")
    val morn: Double,
)

@JsonClass(generateAdapter = true)
data class MinutelyItem(

    @Json(name = "dt")
    val dt: Int,

    @Json(name = "precipitation")
    val precipitation: Int,
)
@Parcelize
@JsonClass(generateAdapter = true)
data class LocationDetails(
    var latLng: LatLng = LatLng(0.0, 0.0),
    var address: String = "",
    var lastDate: String = "",
) : Parcelable