package com.android.reclaim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.android.reclaim.ui.MainScreen
import com.android.reclaim.ui.theme.ReclaimTheme
import com.android.reclaim.util.PreferencesManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val prefs = remember { PreferencesManager(applicationContext) }
            val appTheme by prefs.appThemeFlow.collectAsState()

            val isDarkTheme = when (appTheme) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            ReclaimTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
