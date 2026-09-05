package com.android.reclaim.util

import java.util.Calendar
import java.util.Date

data class SoberTime(
    val years: Int,
    val months: Int,
    val days: Int,
    val totalDays: Int
)

object SoberTimeManager {
    fun calculate(startDate: Date): SoberTime {
        val startCal = Calendar.getInstance().apply { time = startDate }
        val nowCal = Calendar.getInstance()

        var years = nowCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)
        var months = nowCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH)
        var days = nowCal.get(Calendar.DAY_OF_MONTH) - startCal.get(Calendar.DAY_OF_MONTH)

        if (days < 0) {
            months -= 1
            val tempCal = Calendar.getInstance().apply {
                time = nowCal.time
                add(Calendar.MONTH, -1)
            }
            days += tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        if (months < 0) {
            years -= 1
            months += 12
        }

        val diffMillis = nowCal.timeInMillis - startCal.timeInMillis
        val totalDays = (diffMillis / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)

        return SoberTime(
            years = years.coerceAtLeast(0),
            months = months.coerceAtLeast(0),
            days = days.coerceAtLeast(0),
            totalDays = totalDays
        )
    }
}
