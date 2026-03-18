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
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val selectedTeam by viewModel.selectedTeam.collectAsState()
            val isLoading by viewModel.isLoading.collectAsState()

            if (isLoading) return@setContent

            LaunchedEffect(selectedTeam) {
                enableEdgeToEdge(
                    statusBarStyle = if (selectedTeam != null) {
                        //그외 화면 - 상태바 아이콘 색상 흰색
                        SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    } else {
                        //Onboarding 화면 - 상태바 아이콘 색상 유지 (검은색)
                        SystemBarStyle.light(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT
                        )
                    }
                )
            }

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
                    }
                    composable<Home> {
                        HomeScreen(
                            selectedTeam = selectedTeam!!, //null 이면 Onboarding 화면 으로 이동 되기 때문에 !!처리
                            onComplete = {
                                navController.navigate(AddRecord)
                            }
                        )
                    }
                    composable<AddRecord> {
                        AddRecordScreen()
                    }
                }
            }
        }
    }
}