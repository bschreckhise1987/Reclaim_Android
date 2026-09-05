package com.android.reclaim.data.repository

import com.android.reclaim.config.SupabaseConfig
import com.android.reclaim.data.model.CheckIn
import com.android.reclaim.data.model.NewCheckIn
import com.android.reclaim.util.TimestampParser
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Serializable
private data class CheckInRow(val created_at: String)

class CheckInRepository {
    private val postgrest = SupabaseConfig.client.postgrest

    suspend fun checkIfCheckedInToday(userId: String): Boolean {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val result = postgrest["check_ins"]
            .select {
                filter {
                    eq("user_id", userId)
                    gte("created_at", todayStr)
                }
            }
            .decodeList<CheckInRow>()

        return result.isNotEmpty()
    }

    suspend fun submitCheckIn(newCheckIn: NewCheckIn): Boolean {
        postgrest["check_ins"].insert(newCheckIn)
        return true
    }

    suspend fun getWeeklyCheckedInDays(userId: String): List<Int> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        val startOfWeekStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)

        val rows = postgrest["check_ins"]
            .select {
                filter {
                    eq("user_id", userId)
                    gte("created_at", startOfWeekStr)
                }
            }
            .decodeList<CheckInRow>()

        val days = mutableListOf<Int>()
        for (row in rows) {
            val date = TimestampParser.parse(row.created_at) ?: continue
            val cal = Calendar.getInstance().apply { time = date }
            var weekday = cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY
            if (weekday < 0) weekday += 7
            if (weekday in 0..6) {
                days.add(weekday)
            }
        }
        return days.distinct()
    }

    suspend fun getCheckInsHistory(userId: String, offset: Long, limit: Long): List<CheckIn> {
        return postgrest["check_ins"]
            .select {
                filter {
                    eq("user_id", userId)
                }
                order("created_at", order = Order.DESCENDING)
                range(offset, offset + limit - 1)
            }
            .decodeList<CheckIn>()
    }
}
