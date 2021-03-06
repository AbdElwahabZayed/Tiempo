package com.iti.tiempo.base.utils

import android.annotation.SuppressLint
import android.widget.TextView
import com.iti.tiempo.R
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.Temp
import com.iti.tiempo.utils.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("SetTextI18n")
fun TextView.setTextWithCurrentTempType(appSharedPreference: AppSharedPreference) {
    this.text =
        "\u00B0" + when (appSharedPreference.getStringValue(CURRENT_TEMP_TYPE, Fahrenheit)) {
            Fahrenheit -> context.resources.getString(R.string.fahrenheit_sign)
            Celsius -> context.resources.getString(R.string.celsius_sign)
            else -> context.resources.getString(R.string.kelvin_sign)
        }
}

fun TextView.setTextWithCurrentTempValue(appSharedPreference: AppSharedPreference, value: Double) {
    this.text = when (appSharedPreference.getStringValue(CURRENT_TEMP_TYPE, Fahrenheit)) {
        Fahrenheit -> "${value.toFah().toInt()}"
        Celsius -> "${value.toCel().toInt()}"
        else -> "${value.toInt()}"
    }
}

fun TextView.setTextWithCurrentTempValueWithType(
    appSharedPreference: AppSharedPreference,
    value: Double,
) {
    this.text = when (appSharedPreference.getStringValue(CURRENT_TEMP_TYPE, Fahrenheit)) {
        Fahrenheit -> "${
            value.toFah().toInt()
        }°${context.resources.getString(R.string.fahrenheit_sign)}"
        Celsius -> "${value.toCel().toInt()} °${context.resources.getString(R.string.celsius_sign)}"
        else -> "${value.toInt()}°${context.resources.getString(R.string.kelvin_sign)}"
    }
}

fun TextView.setTextWithCurrentTempValueWithType(
    appSharedPreference: AppSharedPreference,
    value: Temp,
) {
    this.text = when (appSharedPreference.getStringValue(CURRENT_TEMP_TYPE, Fahrenheit)) {
        Fahrenheit -> "${value.min.toFah().toInt()}/${
            value.max.toFah().toInt()
        }°${context.resources.getString(R.string.fahrenheit_sign)}"
        Celsius -> "${value.min.toCel().toInt()}/${
            value.max.toCel().toInt()
        }°${context.resources.getString(R.string.celsius_sign)}"
        else -> "${value.min.toInt()}/${value.max.toInt()}°${context.resources.getString(R.string.kelvin_sign)}"
    }
}

@SuppressLint("SetTextI18n")
fun TextView.setValueWithPercentage(clouds: Int) {
    this.text = "$clouds%"
}

@SuppressLint("SetTextI18n")
fun TextView.setVisibilityWithUnit(visibility: Int) {
    this.text = "$visibility ${context.resources.getString(R.string.visi_unit)}"
}

@SuppressLint("SetTextI18n")
fun TextView.setWindWithUnit(appSharedPreference: AppSharedPreference, speed: Double) {
    this.text = when (appSharedPreference.getStringValue(WIND_SPEED_TYPE, METER_SEC)) {
        METER_SEC -> "$speed ${context.resources.getString(R.string.meter_per_sec_sign)}"
        else -> "${(speed * 2.2 * 100.0).roundToInt() / 100.0} ${context.resources.getString(R.string.mile_per_sec_sign)}"
    }
}

@SuppressLint("SetTextI18n")
fun TextView.setPressureWithUnit(pressure: Int) {
    this.text = "$pressure ${context.resources.getString(R.string.pressure_sign)}"
}

////1.8*(K-273) + 32.
fun Double.toFah(): Double {
    return (1.8 * (this - 273)) + 32
}

//C = K - 273.15
fun Double.toCel(): Double {
    return (this - 273.15)
}

fun Double.tokel(): Double {
    return (5 / 9) * (this + 459.67)
}

@SuppressLint("SimpleDateFormat")
fun TextView.setTimeForHourFromTimeStamp(
    time: Long,
    appSharedPreference: AppSharedPreference,
    factor: Long = 1000L,
    pattern: String = "hh aa",
) {
    text = getDateTime(pattern, time, appSharedPreference, factor)
}

fun getDateTime(
    pattern: String,
    time: Long,
    appSharedPreference: AppSharedPreference,
    factor: Long,
): String {
    val sdf = SimpleDateFormat(pattern, Locale(appSharedPreference.getStringValue(LOCALE, "en")))
    sdf.timeZone = TimeZone.getTimeZone("GMT+2")
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(Date(time * factor))
}

@SuppressLint("SimpleDateFormat")
fun getDateTime(pattern: String, time: Long): String {
    val sdf = SimpleDateFormat(pattern)
    sdf.timeZone = TimeZone.getTimeZone("GMT+2")
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(Date(time))
}

@SuppressLint("SimpleDateFormat")
fun TextView.setTextToDayFromTimeStamp(
    time: Long,
    appSharedPreference: AppSharedPreference,
    factor: Long = 1000L,
) {
    text = getDateTime("EEE", time, appSharedPreference, factor)
}

fun TextView.setDateFromTimeStamp(time: Long) {
    text = getDateTime("MM/dd/yy", time)
}