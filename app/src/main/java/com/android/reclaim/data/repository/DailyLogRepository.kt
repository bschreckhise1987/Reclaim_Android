package com.android.reclaim.data.repository

import com.android.reclaim.config.SupabaseConfig
import com.android.reclaim.data.model.DailyLog
import com.android.reclaim.data.model.NewDailyLog
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

class DailyLogRepository {
    private val postgrest = SupabaseConfig.client.postgrest

    suspend fun fetchLogs(userId: String): List<DailyLog> {
        return postgrest["daily_logs"]
            .select {
                filter {
                    eq("user_id", userId)
                }
                order("created_at", order = Order.DESCENDING)
            }
            .decodeList<DailyLog>()
    }

    suspend fun fetchLogsHistory(userId: String, offset: Long, limit: Long): List<DailyLog> {
        return postgrest["daily_logs"]
            .select {
                filter {
                    eq("user_id", userId)
                }
                order("created_at", order = Order.DESCENDING)
                range(offset, offset + limit - 1)
            }
            .decodeList<DailyLog>()
    }

    suspend fun createLog(newLog: NewDailyLog): Boolean {
        postgrest["daily_logs"].insert(newLog)
        return true
    }
}
