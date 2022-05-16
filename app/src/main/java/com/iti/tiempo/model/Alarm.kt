package com.iti.tiempo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
@Entity
data class Alarm(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var fromDate: Long,
    var toDate: Long,
    val time: Long,
    val type: Int,
)