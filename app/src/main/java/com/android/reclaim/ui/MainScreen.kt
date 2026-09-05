package com.android.reclaim.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.reclaim.data.model.CopingStrategy
import com.android.reclaim.ui.auth.AuthViewModel
import com.android.reclaim.ui.auth.LoginScreen
import com.android.reclaim.ui.checkin.CheckInViewModel
import com.android.reclaim.ui.checkin.DailyCheckInSheet
import com.android.reclaim.ui.dailylog.DailyLogEntrySheet
import com.android.reclaim.ui.dailylog.DailyLogEntryViewModel
import com.android.reclaim.ui.dailylog.DailyLogViewModel
import com.android.reclaim.ui.history.HistoryScreen
import com.android.reclaim.ui.history.HistoryViewModel
import com.android.reclaim.ui.home.HomeScreen
import com.android.reclaim.ui.home.HomeViewModel
import com.android.reclaim.ui.insights.InsightsScreen
import com.android.reclaim.ui.insights.InsightsViewModel
import com.android.reclaim.ui.profile.ProfileEditScreen
import com.android.reclaim.ui.profile.ProfileScreen
import com.android.reclaim.ui.profile.ProfileViewModel
import com.android.reclaim.ui.settings.AccountSettingsScreen
import com.android.reclaim.ui.settings.AppearanceSettingsScreen
import com.android.reclaim.ui.settings.BackupSyncScreen
import com.android.reclaim.ui.settings.ChangeEmailScreen
import com.android.reclaim.ui.settings.ChangePasswordScreen
import com.android.reclaim.ui.settings.NotificationSettingsScreen
import com.android.reclaim.ui.settings.PrivacySettingsScreen
import com.android.reclaim.ui.settings.SettingsScreen
import com.android.reclaim.ui.settings.SettingsViewModel
import com.android.reclaim.ui.settings.SupportScreen
import com.android.reclaim.ui.strategy.AddStrategyScreen
import com.android.reclaim.ui.strategy.CopingStrategyGuideScreen
import com.android.reclaim.ui.strategy.StrategyDetailScreen
import com.android.reclaim.ui.strategy.StrategyListScreen
import com.android.reclaim.ui.strategy.StrategyViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    checkInViewModel: CheckInViewModel = viewModel(),
    dailyLogViewModel: DailyLogViewModel = viewModel(),
    dailyLogEntryViewModel: DailyLogEntryViewModel = viewModel(),
    historyViewModel: HistoryViewModel = viewModel(),
    strategyViewModel: StrategyViewModel = viewModel(),
    insightsViewModel: InsightsViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val userId = authViewModel.currentUserId

    val navController = rememberNavController()

    var showCheckInSheet by remember { mutableStateOf(false) }
    var showDailyLogSheet by remember { mutableStateOf(false) }
    var selectedStrategy by remember { mutableStateOf<CopingStrategy?>(null) }

    if (!isAuthenticated) {
        LoginScreen(viewModel = authViewModel)
    } else {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        val bottomTabRoutes = listOf("home", "history", "insights", "profile")
        val showBottomBar = currentDestination in bottomTabRoutes

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentDestination == "home",
                            onClick = { navController.navigate("home") { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = currentDestination == "history",
                            onClick = { navController.navigate("history") { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.History, contentDescription = "History") },
                            label = { Text("History") }
                        )
                        NavigationBarItem(
                            selected = currentDestination == "insights",
                            onClick = { navController.navigate("insights") { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.BarChart, contentDescription = "Insights") },
                            label = { Text("Insights") }
                        )
                        NavigationBarItem(
                            selected = currentDestination == "profile",
                            onClick = { navController.navigate("profile") { launchSingleTop = true } },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile") }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                composable("home") {
                    HomeScreen(
                        userId = userId,
                        profileVM = profileViewModel,
                        checkInVM = checkInViewModel,
                        dailyLogVM = dailyLogViewModel,
                        strategyVM = strategyViewModel,
                        homeVM = homeViewModel,
                        onStartCheckIn = { showCheckInSheet = true },
                        onAddDailyLog = { showDailyLogSheet = true },
                        onOpenStrategies = { navController.navigate("strategies") },
                        onOpenStrategyDetail = { strategy ->
                            selectedStrategy = strategy
                            navController.navigate("strategy_detail")
                        }
                    )
                }

                composable("history") {
                    HistoryScreen(
                        userId = userId,
                        viewModel = historyViewModel
                    )
                }

                composable("insights") {
                    InsightsScreen(
                        userId = userId,
                        viewModel = insightsViewModel
                    )
                }

                composable("profile") {
                    ProfileScreen(
                        userId = userId,
                        viewModel = profileViewModel,
                        onEditProfile = { navController.navigate("profile_edit") },
                        onOpenSettings = { navController.navigate("settings") }
                    )
                }

                composable("profile_edit") {
                    ProfileEditScreen(
                        userId = userId,
                        viewModel = profileViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("strategies") {
                    StrategyListScreen(
                        userId = userId,
                        viewModel = strategyViewModel,
                        onBack = { navController.popBackStack() },
                        onOpenDetail = { strategy ->
                            selectedStrategy = strategy
                            navController.navigate("strategy_detail")
                        },
                        onAddStrategy = { navController.navigate("add_strategy") },
                        onOpenGuide = { navController.navigate("strategy_guide") }
                    )
                }

                composable("strategy_detail") {
                    selectedStrategy?.let { strategy ->
                        StrategyDetailScreen(
                            userId = userId,
                            strategy = strategy,
                            viewModel = strategyViewModel,
                            onBack = { navController.popBackStack() },
                            onEdit = {}
                        )
                    }
                }

                composable("add_strategy") {
                    AddStrategyScreen(
                        userId = userId,
                        viewModel = strategyViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("strategy_guide") {
                    CopingStrategyGuideScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("settings") {
                    SettingsScreen(
                        viewModel = settingsViewModel,
                        onBack = { navController.popBackStack() },
                        onOpenAccount = { navController.navigate("account_settings") },
                        onOpenAppearance = { navController.navigate("appearance_settings") },
                        onOpenNotifications = { navController.navigate("notification_settings") },
                        onOpenPrivacy = { navController.navigate("privacy_settings") },
                        onOpenBackup = { navController.navigate("backup_sync") },
                        onOpenSupport = { navController.navigate("support") },
                        onSignedOut = {
                            profileViewModel.clear()
                        }
                    )
                }

                composable("account_settings") {
                    AccountSettingsScreen(
                        userEmail = authViewModel.email,
                        onBack = { navController.popBackStack() },
                        onChangeEmail = { navController.navigate("change_email") },
                        onChangePassword = { navController.navigate("change_password") }
                    )
                }

                composable("change_email") {
                    ChangeEmailScreen(
                        authViewModel = authViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("change_password") {
                    ChangePasswordScreen(
                        authViewModel = authViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("appearance_settings") {
                    AppearanceSettingsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("notification_settings") {
                    NotificationSettingsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("privacy_settings") {
                    PrivacySettingsScreen(
                        authViewModel = authViewModel,
                        onBack = { navController.popBackStack() },
                        onDeletedAccount = {
                            profileViewModel.clear()
                        }
                    )
                }

                composable("backup_sync") {
                    BackupSyncScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("support") {
                    SupportScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }

        if (showCheckInSheet && userId != null) {
            DailyCheckInSheet(
                userId = userId,
                viewModel = checkInViewModel,
                onDismiss = { showCheckInSheet = false }
            )
        }

        if (showDailyLogSheet && userId != null) {
            DailyLogEntrySheet(
                userId = userId,
                viewModel = dailyLogEntryViewModel,
                onDismiss = { showDailyLogSheet = false }
            )
        }
    }
}
