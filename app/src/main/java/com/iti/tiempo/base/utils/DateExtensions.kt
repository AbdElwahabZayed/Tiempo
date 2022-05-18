package com.iti.tiempo.base.utils

import android.text.format.DateFormat
import java.util.*


fun Date.getDateHomeStyleString(): String {
    val dayOfTheWeek = DateFormat.format("EEEE", this) as String // Thursday

    val day = DateFormat.format("dd", this) as String // 20

    val monthString = DateFormat.format("MMM", this) as String // Jun

    return "$dayOfTheWeek, $day $monthString"
}


fun isTodayInRange(start: Date?, end: Date?): Boolean {
    return getTodayDate().isInRange(
        start,
        end)
}

fun Long.getDate(): Date {
    return Date(this)
}

fun Long.getCalender(): Calendar {
    return Calendar.getInstance().apply { timeInMillis = this@getCalender}
}

fun Date.isInRange(start: Date?, end: Date?): Boolean {
    return this.before(end) && this.after(start)
}

fun getTodayDate(): Date {
    val c = Calendar.getInstance()

    // set the calendar to start of today
    c[Calendar.HOUR_OF_DAY] = 0
    c[Calendar.MINUTE] = 0
    c[Calendar.SECOND] = 0
    c[Calendar.MILLISECOND] = 0

    // and get that as a Date
    return c.time
}

fun Long.truncateToDate(): Long {
    val cal = Calendar.getInstance() // locale-specific
    cal.timeInMillis = this
    cal[Calendar.HOUR_OF_DAY] = 0
    cal[Calendar.MINUTE] = 0
    cal[Calendar.SECOND] = 0
    cal[Calendar.MILLISECOND] = 0
    return cal.timeInMillis
}

fun Long.truncateToHours(): Long {
    val cal = Calendar.getInstance() // locale-specific
    cal.timeInMillis = this
    cal[Calendar.YEAR] = 0
    cal[Calendar.MONTH] = 0
    cal[Calendar.DAY_OF_WEEK] = 0
    cal[Calendar.DAY_OF_MONTH] = 0
    return cal.timeInMillis
}
