package com.android.reclaim.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("reclaim_prefs", Context.MODE_PRIVATE)

    var appTheme: String
        get() = prefs.getString("appTheme", "system") ?: "system"
        set(value) = prefs.edit().putString("appTheme", value).apply()

    var accentColor: String
        get() = prefs.getString("accentColor", "blue") ?: "blue"
        set(value) = prefs.edit().putString("accentColor", value).apply()

    var reduceMotion: Boolean
        get() = prefs.getBoolean("reduceMotion", false)
        set(value) = prefs.edit().putBoolean("reduceMotion", value).apply()

    var largeText: Boolean
        get() = prefs.getBoolean("largeText", false)
        set(value) = prefs.edit().putBoolean("largeText", value).apply()

    var highContrast: Boolean
        get() = prefs.getBoolean("highContrast", false)
        set(value) = prefs.edit().putBoolean("highContrast", value).apply()

    var appLockEnabled: Boolean
        get() = prefs.getBoolean("appLockEnabled", false)
        set(value) = prefs.edit().putBoolean("appLockEnabled", value).apply()

    var hideStreak: Boolean
        get() = prefs.getBoolean("hideStreak", false)
        set(value) = prefs.edit().putBoolean("hideStreak", value).apply()

    var hideProfilePhoto: Boolean
        get() = prefs.getBoolean("hideProfilePhoto", false)
        set(value) = prefs.edit().putBoolean("hideProfilePhoto", value).apply()

    var hideSoberDate: Boolean
        get() = prefs.getBoolean("hideSoberDate", false)
        set(value) = prefs.edit().putBoolean("hideSoberDate", value).apply()

    var dailyReminderEnabled: Boolean
        get() = prefs.getBoolean("dailyReminderEnabled", false)
        set(value) = prefs.edit().putBoolean("dailyReminderEnabled", value).apply()

    var dailyReminderHour: Int
        get() = prefs.getInt("dailyReminderHour", 20)
        set(value) = prefs.edit().putInt("dailyReminderHour", value).apply()

    var dailyReminderMinute: Int
        get() = prefs.getInt("dailyReminderMinute", 0)
        set(value) = prefs.edit().putInt("dailyReminderMinute", value).apply()
}
