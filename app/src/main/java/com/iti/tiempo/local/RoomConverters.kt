package com.iti.tiempo.local

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.iti.tiempo.model.*
import com.iti.tiempo.utils.MoshiHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class RoomConverters {
    private val moshiHelper = MoshiHelper(Moshi.Builder().build())

    @TypeConverter
    fun getDailyListFromJson(json: String): List<DailyItem>? {
        val type = Types.newParameterizedType(List::class.java, DailyItem::class.java)
        return moshiHelper.getListOfObjFromJsonString(type, json)
    }

    @TypeConverter
    fun getJsonFromDailyList(dailyItems: List<DailyItem>): String {
        val type: Type = Types.newParameterizedType(
            List::class.java,
            DailyItem::class.java
        )
        return moshiHelper.getJsonStringFromListOfObject(type,
            dailyItems)
    }

    @TypeConverter
    fun getListHourlyItemFromJson(json: String): List<HourlyItem>? {
        val type = Types.newParameterizedType(List::class.java, HourlyItem::class.java)
        return moshiHelper.getListOfObjFromJsonString(type, json)
    }

    @TypeConverter
    fun getJsonFromListOfHourlyItem(hourlyItem: List<HourlyItem>): String {
        val type: Type = Types.newParameterizedType(
            List::class.java,
            HourlyItem::class.java
        )
        return moshiHelper.getJsonStringFromListOfObject(type,
            hourlyItem)
    }

    @TypeConverter
    fun getListMinutelyItemFromJson(json: String): List<MinutelyItem>? {
        val type = Types.newParameterizedType(List::class.java, MinutelyItem::class.java)
        return moshiHelper.getListOfObjFromJsonString(type, json)
    }

    @TypeConverter
    fun getJsonFromListMinutelyItem(minutelyItems: List<MinutelyItem>?): String {
        val type: Type = Types.newParameterizedType(
            List::class.java,
            MinutelyItem::class.java
        )
        return when (minutelyItems) {
            null -> ""
            else -> moshiHelper.getJsonStringFromListOfObject(type,
                minutelyItems)
        }
    }

    @TypeConverter
    fun getStringFromCurrent(current: Current): String {
        return moshiHelper.getJsonStringFromObject(Current::class.java, current)
    }

    @TypeConverter
    fun getCurrentFromString(current: String): Current? {
        return moshiHelper.getObjFromJsonString(Current::class.java, current)
    }

    @TypeConverter
    fun getLatLonFromString(json: String): LatLng? {
        return moshiHelper.getObjFromJsonString(LatLng::class.java, json)
    }

    @TypeConverter
    fun getJsonFromLatLon(latLng: LatLng): String{
        return moshiHelper.getJsonStringFromObject(LatLng::class.java,latLng)
    }

}