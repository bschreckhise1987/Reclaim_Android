package com.android.reclaim.config

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseConfig {
    const val URL = "https://myfknsoytmxudnvcsmkp.supabase.co"
    const val ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im15Zmtuc295dG14dWRudmNzbWtwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzcwODkzMTIsImV4cCI6MjA5MjY2NTMxMn0.Sq8--Xg6MlM6AYHdMFVc28xrUykBOH5GFX5vRsrCq2k"
    const val STORAGE_PUBLIC_BASE = "$URL/storage/v1/object/public"

    val client = createSupabaseClient(
        supabaseUrl = URL,
        supabaseKey = ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
