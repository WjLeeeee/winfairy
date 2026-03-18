package com.woojin.winfairy.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.woojin.winfairy.core.model.KboTeam

fun KboTeam.primaryColor(): Color = when (this) {
    KboTeam.DooSan -> Color(0xFF1D1D4E)
    KboTeam.Lg -> Color(0xFFC60C30)
    KboTeam.Kiwoom -> Color(0xFF820024)
    KboTeam.Kt -> Color(0xFF000000)
    KboTeam.Ssg -> Color(0xFFCE0E2D)
    KboTeam.Kia -> Color(0xFFEA0029)
    KboTeam.HanWha -> Color(0xFFFF6600)
    KboTeam.Nc -> Color(0xFF1D467D)
    KboTeam.Lotte -> Color(0xFF002955)
    KboTeam.Samsung -> Color(0xFF074CA1)
}

fun KboTeam.onPrimaryColor(): Color = when(this) {
    KboTeam.DooSan -> Color(0xFFFFFFFF)
    KboTeam.Lg -> Color(0xFF000000)
    KboTeam.Kiwoom -> Color(0xFFD4A76A)
    KboTeam.Kt -> Color(0xFFEB1C2D)
    KboTeam.Ssg -> Color(0xFF000000)
    KboTeam.Kia -> Color(0xFF000000)
    KboTeam.HanWha -> Color(0xFF000000)
    KboTeam.Nc -> Color(0xFFC6960B)
    KboTeam.Lotte -> Color(0xFFD00F31)
    KboTeam.Samsung -> Color(0xFFFFFFFF)
}

fun teamLightColorScheme(team: KboTeam): ColorScheme = lightColorScheme(
    primary = team.primaryColor(),
    onPrimary = team.onPrimaryColor(),
    background = Color(0xFFF3F6F3),
    onBackground = Color(0xFF000000),
    onSurfaceVariant = Color(0xFFB0B0B0),
)

fun teamDarkColorScheme(team: KboTeam): ColorScheme = lightColorScheme(
    primary = team.primaryColor(),
    onPrimary = team.onPrimaryColor(),
    background = Color(0xFFF3F6F3),
    onBackground = Color(0xFF000000),
    onSurfaceVariant = Color(0xFFB0B0B0),
)

fun defaultDarkColorScheme(): ColorScheme = lightColorScheme(
    background = Color(0xFFF3F6F3),
    onBackground = Color(0xFF000000),
    onSurfaceVariant = Color(0xFFB0B0B0),
)

fun defaultColorScheme(): ColorScheme = lightColorScheme(
    background = Color(0xFFF3F6F3),
    onBackground = Color(0xFF000000),
    onSurfaceVariant = Color(0xFFB0B0B0),
)