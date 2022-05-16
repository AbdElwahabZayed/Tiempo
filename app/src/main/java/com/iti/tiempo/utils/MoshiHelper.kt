package com.iti.tiempo.utils

import android.util.Log
import com.squareup.moshi.*
import java.io.EOFException
import java.lang.reflect.Type
import javax.inject.Inject

private const val TAG = "MoshiHelper"

class MoshiHelper @Inject constructor(private val moshi: Moshi) {

    fun <T : Any> getJsonStringFromObject(modelClass: Class<T>, obj: T): String {
        return moshi.adapter(modelClass).toJson(obj)
    }

    fun <T : Any> getObjFromJsonString(modelClass: Class<T>, json: String): T? {
        return try {
            moshi.adapter(modelClass).fromJson(json)
        } catch (e: JsonDataException) {
            Log.e(TAG, "getObjFromJsonString: ", e)
            null
        }catch (ex:EOFException){
            null
        }
    }

    fun <T : Any>  getJsonStringFromListOfObject(collectionClass: Type,  obj: List<T>): String {
        return try {
            moshi.adapter<List<T>>(collectionClass).toJson(obj)
        }catch (e:EOFException){
            ""
        }
    }

    fun <T : Any> getListOfObjFromJsonString(collectionClass: Type, json: String): List<T>? {
        return try {
            moshi.adapter<List<T>>(collectionClass).fromJson(json)
        }catch (ex:EOFException) {
            null
        }catch (e: JsonDataException ) {
            Log.e(TAG, "getListOfObjFromJsonString: ", e)
            null
        }
    }
}