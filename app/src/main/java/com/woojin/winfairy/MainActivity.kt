package com.woojin.winfairy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.woojin.winfairy.core.designsystem.theme.WinFairyTheme
import com.woojin.winfairy.core.navigation.AddRecord
import com.woojin.winfairy.core.navigation.Home
import com.woojin.winfairy.core.navigation.Onboarding
import com.woojin.winfairy.feature.home.HomeScreen
import com.woojin.winfairy.feature.onboarding.OnboardingScreen
import com.woojin.winfairy.feature.record.AddRecordScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WinFairyTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Onboarding
                ) {
                    composable<Onboarding> { OnboardingScreen() }  // feature:onboarding에서 가져옴
                    composable<Home> { HomeScreen() }              // feature:home에서 가져옴
                    composable<AddRecord> { AddRecordScreen() }    // feature:record에서 가져옴
                }
            }
        }
    }
}