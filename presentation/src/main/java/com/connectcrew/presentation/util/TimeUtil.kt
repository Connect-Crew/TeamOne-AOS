package com.connectcrew.presentation.util

import android.content.Context
import com.connectcrew.presentation.R
import java.time.Duration
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

        @JvmStatic
        fun getDateTimeFormatStringForTimeDifference(dateTime: String?, context: Context): String {
            val targetDateTime = dateTime?.let { ZonedDateTime.parse(dateTime) } ?: ZonedDateTime.now()
            val currentDateTime = ZonedDateTime.now()
            val duration = Duration.between(targetDateTime, currentDateTime)

            return when {
                duration.seconds < TIME_ONE_MINUTE -> context.getString(R.string.date_format_time_difference_second)
                duration.seconds < TIME_ONE_HOUR -> context.getString(R.string.date_format_time_difference_minute, (duration.seconds / TIME_ONE_MINUTE).toString())
                duration.seconds < TIME_ONE_DAY -> context.getString(R.string.date_format_time_difference_hour, (duration.seconds / TIME_ONE_HOUR).toString())
                duration.seconds < TIME_ONE_WEEK -> context.getString(R.string.date_format_time_difference_day, (duration.seconds / TIME_ONE_DAY).toString())
                else -> targetDateTime.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(context.getString(R.string.date_format_time_date)))
            }
        }

        private const val TIME_ONE_MINUTE = 60
        private const val TIME_ONE_HOUR = 3600
        private const val TIME_ONE_DAY = 86400
        private const val TIME_ONE_WEEK = 604800
    }
}