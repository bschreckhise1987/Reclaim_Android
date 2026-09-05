package com.android.reclaim.data.repository

import com.android.reclaim.config.SupabaseConfig
import com.android.reclaim.data.model.Streak
import com.android.reclaim.data.model.StreakUpdatePayload
import com.android.reclaim.util.TimestampParser
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import java.util.Calendar
import java.util.Date

class StreakRepository {
    private val postgrest = SupabaseConfig.client.postgrest

    suspend fun updateStreak(userId: String) {
        val streak = postgrest["streaks"]
            .select {
                filter { eq("user_id", userId) }
            }
            .decodeSingleOrNull<Streak>() ?: return

        val lastUpdate = TimestampParser.parse(streak.updatedAt) ?: Date()
        val now = Date()

        val calLast = Calendar.getInstance().apply { time = lastUpdate }
        val calNow = Calendar.getInstance().apply { time = now }

        val daysSince = ((calNow.timeInMillis - calLast.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

        var newCurrent = streak.currentStreak
        var newLongest = streak.longestStreak

        if (daysSince == 1) {
            newCurrent += 1
        } else if (daysSince > 1) {
            newCurrent = 1
        }

        if (newCurrent > newLongest) {
            newLongest = newCurrent
        }

        val payload = StreakUpdatePayload(currentStreak = newCurrent, longestStreak = newLongest)

        postgrest["streaks"].update(payload) {
            filter { eq("id", streak.id) }
        }
    }

    suspend fun getStreaksHistory(userId: String, offset: Long, limit: Long): List<Streak> {
        return postgrest["streaks"]
            .select {
                filter { eq("user_id", userId) }
                order("updated_at", order = Order.DESCENDING)
                range(offset, offset + limit - 1)
            }
            .decodeList<Streak>()
    }
}
