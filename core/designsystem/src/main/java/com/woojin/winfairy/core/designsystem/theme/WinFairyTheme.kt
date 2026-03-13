package com.woojin.winfairy.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.woojin.winfairy.core.model.KboTeam

@Composable
fun WinFairyTheme(
    team: KboTeam? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        team != null && darkTheme -> teamDarkColorScheme(team)
        team != null -> teamLightColorScheme(team)
        darkTheme -> darkColorScheme()  // 기본 다크
        else -> lightColorScheme()      // 기본 라이트
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}