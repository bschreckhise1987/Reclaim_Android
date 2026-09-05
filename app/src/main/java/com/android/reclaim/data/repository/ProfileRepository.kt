package com.android.reclaim.data.repository

import com.android.reclaim.config.SupabaseConfig
import com.android.reclaim.data.model.Profile
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ProfileRepository {
    private val postgrest = SupabaseConfig.client.postgrest
    private val storage = SupabaseConfig.client.storage

    suspend fun getProfile(userId: String): Profile? {
        return postgrest["profiles"]
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingleOrNull<Profile>()
    }

    suspend fun updateName(userId: String, name: String) {
        postgrest["profiles"].update(
            buildJsonObject { put("full_name", name) }
        ) {
            filter { eq("id", userId) }
        }
    }

    suspend fun updateBio(userId: String, bio: String) {
        postgrest["profiles"].update(
            buildJsonObject { put("bio", bio) }
        ) {
            filter { eq("id", userId) }
        }
    }

    suspend fun updateSoberStartDate(userId: String, dateString: String) {
        postgrest["profiles"].update(
            buildJsonObject { put("sober_start_date", dateString) }
        ) {
            filter { eq("id", userId) }
        }
    }

    suspend fun uploadProfilePhoto(userId: String, jpegData: ByteArray): String {
        val fileName = "$userId.jpg"
        val bucket = storage["profile_photos"]

        bucket.upload(fileName, jpegData) {
            upsert = true
        }

        val publicUrl = "${SupabaseConfig.STORAGE_PUBLIC_BASE}/profile_photos/$fileName?t=${System.currentTimeMillis()}"

        postgrest["profiles"].update(
            buildJsonObject { put("photo_url", publicUrl) }
        ) {
            filter { eq("id", userId) }
        }

        return publicUrl
    }
}
