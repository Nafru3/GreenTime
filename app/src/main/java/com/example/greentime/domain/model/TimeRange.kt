package com.example.greentime.domain.model

import java.util.Calendar

data class TimeRange(val startMs: Long, val endMs: Long) {
    companion object {
        fun today(): TimeRange {
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            return TimeRange(cal.timeInMillis, System.currentTimeMillis())
        }

        fun lastDays(days: Int): TimeRange {
            val now = System.currentTimeMillis()
            return TimeRange(now - days * 24L * 3_600_000L, now)
        }
    }
}
