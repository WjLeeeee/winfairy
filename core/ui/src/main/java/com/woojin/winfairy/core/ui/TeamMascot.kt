package com.woojin.winfairy.core.ui

import com.woojin.winfairy.core.model.KboTeam

fun KboTeam.mascot(): Int = when (this) {
    KboTeam.DooSan -> R.drawable.doosan_mascot
    KboTeam.Lg -> R.drawable.lg_mascot
    KboTeam.Samsung -> R.drawable.samsung_mascot
    KboTeam.Kiwoom -> R.drawable.kiwoom_mascot
    KboTeam.Kt -> R.drawable.kt_mascot
    KboTeam.Ssg -> R.drawable.ssg_mascot
    KboTeam.Kia -> R.drawable.kia_mascot
    KboTeam.HanWha -> R.drawable.hanwha_mascot
    KboTeam.Nc -> R.drawable.nc_mascot
    KboTeam.Lotte -> R.drawable.lotte_mascot
}