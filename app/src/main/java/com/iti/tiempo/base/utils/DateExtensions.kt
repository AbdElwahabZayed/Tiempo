package com.iti.tiempo.base.utils

import android.text.format.DateFormat
import java.util.*


fun Date.getDateHomeStyleString():String{
    val dayOfTheWeek = DateFormat.format("EEEE", this) as String // Thursday

    val day = DateFormat.format("dd", this) as String // 20

    val monthString = DateFormat.format("MMM", this) as String // Jun

    return "$dayOfTheWeek, $day $monthString"
}