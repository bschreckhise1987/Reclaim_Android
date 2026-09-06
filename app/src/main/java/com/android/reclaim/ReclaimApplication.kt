package com.android.reclaim

import android.app.Application
import com.android.reclaim.config.SupabaseConfig

class ReclaimApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SupabaseConfig.init(this)
    }
}
