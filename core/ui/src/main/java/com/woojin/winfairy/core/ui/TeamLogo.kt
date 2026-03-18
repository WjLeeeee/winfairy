package com.woojin.winfairy.core.ui

import com.woojin.winfairy.core.model.KboTeam

fun KboTeam.logoRes(): Int = when (this) {
    KboTeam.DooSan -> R.drawable.logo_doosan
    KboTeam.Lg -> R.drawable.logo_lg
    KboTeam.Samsung -> R.drawable.logo_samsung
    KboTeam.Kiwoom -> R.drawable.logo_kiwoom
    KboTeam.Kt -> R.drawable.logo_kt
    KboTeam.Ssg -> R.drawable.logo_ssg
    KboTeam.Kia -> R.drawable.logo_kia
    KboTeam.HanWha -> R.drawable.logo_hanwha
    KboTeam.Nc -> R.drawable.logo_nc
    KboTeam.Lotte -> R.drawable.logo_lotte
}