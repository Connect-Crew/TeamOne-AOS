package com.connectcrew.presentation.util

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeUtil {

    companion object {

        @JvmStatic
        fun getDateTimeFormatString(zonedDateTime: ZonedDateTime? = null, pattern: String): String {
            return (zonedDateTime ?: ZonedDateTime.now()).withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(pattern))
        }

        @JvmStatic
        fun getDateTimeFormatString(dateTime: String?, pattern: String): String {
            val zonedDateTime = dateTime?.let { ZonedDateTime.parse(dateTime) } ?: ZonedDateTime.now()
            return zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())?.format(DateTimeFormatter.ofPattern(pattern)) ?: ""
        }

        @JvmStatic
        fun getTimeFormatString(time: String?, pattern: String): String {
            val zonedDateTime = time?.split(":")?.let { ZonedDateTime.now().withHour(it[0].toInt()).withMinute(it[1].toInt()) } ?: ZonedDateTime.now()
            return zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(pattern))
        }
    }
}