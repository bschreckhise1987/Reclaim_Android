package com.android.reclaim.util

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("reclaim_prefs", Context.MODE_PRIVATE)

    private val _appTheme = MutableStateFlow(appTheme)
    val appThemeFlow: StateFlow<String> = _appTheme.asStateFlow()

    private val _highContrast = MutableStateFlow(highContrast)
    val highContrastFlow: StateFlow<Boolean> = _highContrast.asStateFlow()

    private val _largeText = MutableStateFlow(largeText)
    val largeTextFlow: StateFlow<Boolean> = _largeText.asStateFlow()

    private val _reduceMotion = MutableStateFlow(reduceMotion)
    val reduceMotionFlow: StateFlow<Boolean> = _reduceMotion.asStateFlow()

    private val _hideStreak = MutableStateFlow(hideStreak)
    val hideStreakFlow: StateFlow<Boolean> = _hideStreak.asStateFlow()

    private val _hideSoberDate = MutableStateFlow(hideSoberDate)
    val hideSoberDateFlow: StateFlow<Boolean> = _hideSoberDate.asStateFlow()

    private val _dailyReminderEnabled = MutableStateFlow(dailyReminderEnabled)
    val dailyReminderEnabledFlow: StateFlow<Boolean> = _dailyReminderEnabled.asStateFlow()

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            "appTheme" -> _appTheme.value = appTheme
            "highContrast" -> _highContrast.value = highContrast
            "largeText" -> _largeText.value = largeText
            "reduceMotion" -> _reduceMotion.value = reduceMotion
            "hideStreak" -> _hideStreak.value = hideStreak
            "hideSoberDate" -> _hideSoberDate.value = hideSoberDate
            "dailyReminderEnabled" -> _dailyReminderEnabled.value = dailyReminderEnabled
        }
    }

    init {
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    var appTheme: String
        get() = prefs.getString("appTheme", "system") ?: "system"
        set(value) {
            prefs.edit().putString("appTheme", value).apply()
            _appTheme.value = value
        }

    var accentColor: String
        get() = prefs.getString("accentColor", "blue") ?: "blue"
        set(value) = prefs.edit().putString("accentColor", value).apply()

    var reduceMotion: Boolean
        get() = prefs.getBoolean("reduceMotion", false)
        set(value) {
            prefs.edit().putBoolean("reduceMotion", value).apply()
            _reduceMotion.value = value
        }

    var largeText: Boolean
        get() = prefs.getBoolean("largeText", false)
        set(value) {
            prefs.edit().putBoolean("largeText", value).apply()
            _largeText.value = value
        }

    var highContrast: Boolean
        get() = prefs.getBoolean("highContrast", false)
        set(value) {
            prefs.edit().putBoolean("highContrast", value).apply()
            _highContrast.value = value
        }

    var appLockEnabled: Boolean
        get() = prefs.getBoolean("appLockEnabled", false)
        set(value) = prefs.edit().putBoolean("appLockEnabled", value).apply()

    var hideStreak: Boolean
        get() = prefs.getBoolean("hideStreak", false)
        set(value) {
            prefs.edit().putBoolean("hideStreak", value).apply()
            _hideStreak.value = value
        }

    var hideProfilePhoto: Boolean
        get() = prefs.getBoolean("hideProfilePhoto", false)
        set(value) = prefs.edit().putBoolean("hideProfilePhoto", value).apply()

    var hideSoberDate: Boolean
        get() = prefs.getBoolean("hideSoberDate", false)
        set(value) {
            prefs.edit().putBoolean("hideSoberDate", value).apply()
            _hideSoberDate.value = value
        }

    var dailyReminderEnabled: Boolean
        get() = prefs.getBoolean("dailyReminderEnabled", false)
        set(value) {
            prefs.edit().putBoolean("dailyReminderEnabled", value).apply()
            _dailyReminderEnabled.value = value
        }

    var dailyReminderHour: Int
        get() = prefs.getInt("dailyReminderHour", 20)
        set(value) = prefs.edit().putInt("dailyReminderHour", value).apply()

    var dailyReminderMinute: Int
        get() = prefs.getInt("dailyReminderMinute", 0)
        set(value) = prefs.edit().putInt("dailyReminderMinute", value).apply()
}
