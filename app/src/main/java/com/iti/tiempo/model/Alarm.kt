package com.iti.tiempo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Entity
@Parcelize
data class Alarm(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var fromDate: Long,
    var toDate: Long,
    val time: Long,
    val type: Int,
) : Parcelable