package org.meetmap.util

import java.util.*

fun getCalendarTime(): Long {
    val cal = Calendar.getInstance(TimeZone.getDefault())
    return cal.timeInMillis
}