package com.woojin.winfairy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.woojin.winfairy.core.designsystem.theme.WinFairyTheme
import com.woojin.winfairy.core.model.KboTeam
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
            val viewModel: MainViewModel = hiltViewModel()
            val selectedTeam by viewModel.selectedTeam.collectAsState()
            val isLoading by viewModel.isLoading.collectAsState()

            if (isLoading) return@setContent

            WinFairyTheme(team = selectedTeam) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = if (selectedTeam == null) Onboarding else Home
                ) {
                    composable<Onboarding> {
                        OnboardingScreen { selectedKboTeam ->
                            viewModel.setTeam(selectedKboTeam)
                            navController.navigate(Home) {
                                popUpTo(Onboarding) { inclusive = true }
                            }
                        }
                    }  // feature:onboarding에서 가져옴
                    composable<Home> {
                        HomeScreen {
                            navController.navigate(AddRecord)
                        }
                    }              // feature:home에서 가져옴
                    composable<AddRecord> {
                        AddRecordScreen()
                    }    // feature:record에서 가져옴
                }
            }
        }
    }
}