package com.woojin.winfairy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.woojin.winfairy.core.designsystem.theme.WinFairyTheme
import com.woojin.winfairy.core.navigation.AddRecord
import com.woojin.winfairy.core.navigation.EditRecord
import com.woojin.winfairy.core.navigation.Home
import com.woojin.winfairy.core.navigation.Onboarding
import com.woojin.winfairy.core.navigation.UpComingGameRecord
import com.woojin.winfairy.feature.home.HomeScreen
import com.woojin.winfairy.feature.onboarding.OnboardingScreen
import com.woojin.winfairy.feature.record.AddTicketScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val selectedTeam by viewModel.selectedTeam.collectAsState()
            val isLoading by viewModel.isLoading.collectAsState()
            var showSplash by rememberSaveable { mutableStateOf(true) }
            // 로딩 끝나면 시스템 Splash 제거
            LaunchedEffect(isLoading) {
                if (!isLoading) {
                    splashScreen.setKeepOnScreenCondition { false }
                }
            }
            if (isLoading) return@setContent
            LaunchedEffect(selectedTeam) {
                if (selectedTeam != null) {
                    delay(1500)
                }
                showSplash = false
            }
            if (showSplash && selectedTeam != null) {
                SplashScreen(myTeam = selectedTeam!!)
                return@setContent
            }

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            LaunchedEffect(currentRoute) {
                enableEdgeToEdge(
                    statusBarStyle = when (currentRoute) {
                        Onboarding::class.qualifiedName, AddRecord::class.qualifiedName -> {
                            SystemBarStyle.light(
                                android.graphics.Color.TRANSPARENT,
                                android.graphics.Color.TRANSPARENT
                            )
                        }

                        else -> SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    }
                )
            }

            val analytics = remember { Firebase.analytics }
            LaunchedEffect(navController) {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    val screenName = when (destination.route) {
                        "com.woojin.winfairy.core.navigation.Home" -> "Home"
                        "com.woojin.winfairy.core.navigation.Onboarding" -> "Onboarding"
                        "com.woojin.winfairy.core.navigation.AddRecord" -> "AddRecord"
                        "com.woojin.winfairy.core.navigation.UpComingGameRecord" -> "UpComingGameRecord"
                        "com.woojin.winfairy.core.navigation.EditRecord" -> "EditRecord"
                        else -> destination.route ?: "unknown"
                    }
                    analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                        param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                    }
                }
            }

            WinFairyTheme(team = selectedTeam) {
                NavHost(
                    navController = navController,
                    startDestination = if (selectedTeam == null) Onboarding else Home
                ) {
                    composable<Onboarding> {
                        OnboardingScreen { selectedKboTeam ->
                            analytics.logEvent("select_team") {
                                param("team_name", selectedKboTeam.teamName)
                            }
                            viewModel.setTeam(selectedKboTeam)
                            navController.navigate(Home) {
                                popUpTo(Onboarding) { inclusive = true }
                            }
                        }
                    }
                    composable<Home> {
                        selectedTeam?.let { team ->
                            HomeScreen(
                                selectedTeam = team,
                                onComplete = {
                                    navController.navigate(AddRecord(it))
                                },
                                onEditRecord = { index, recordId ->
                                    navController.navigate(
                                        EditRecord(
                                            gameNo = index,
                                            recordId = recordId
                                        )
                                    )
                                },
                                recordItem = { id, allGameCnt ->
                                    navController.navigate(
                                        UpComingGameRecord(gameNo = allGameCnt, id = id)
                                    )
                                }
                            )
                        }
                    }
                    composable<AddRecord> { backStackEntry ->
                        selectedTeam?.let { team ->
                            val args = backStackEntry.toRoute<AddRecord>()
                            AddTicketScreen(
                                selectedTeam = team,
                                onComplete = {
                                    navController.popBackStack()
                                },
                                gameNo = args.gameNo,
                            )
                        }
                    }
                    composable<UpComingGameRecord> { backStackEntry ->
                        selectedTeam?.let { team ->
                            val args = backStackEntry.toRoute<UpComingGameRecord>()
                            AddTicketScreen(
                                selectedTeam = team,
                                upComingGameId = args.id,
                                onComplete = {
                                    navController.popBackStack()
                                },
                                gameNo = args.gameNo + 1,
                            )
                        }
                    }
                    composable<EditRecord> { backStackEntry ->
                        selectedTeam?.let { team ->
                            val args = backStackEntry.toRoute<EditRecord>()
                            AddTicketScreen(
                                selectedTeam = team,
                                recordId = args.recordId,
                                onComplete = { navController.popBackStack() },
                                gameNo = args.gameNo,
                            )
                        }
                    }
                }
            }
        }
    }
}