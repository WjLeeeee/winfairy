package com.woojin.winfairy.core.ui

import com.woojin.winfairy.core.model.WinTier

fun WinTier.iconRes(): Int = when (this) {
    WinTier.NO_GAME -> R.drawable.tier_no_game
    WinTier.DEFEAT_TOTEM -> R.drawable.tier_defeat_totem
    WinTier.ANGER_INDUCER -> R.drawable.tier_anger_inducer
    WinTier.HOPE_TORTURER -> R.drawable.tier_hope_torturer
    WinTier.BODHISATTVA -> R.drawable.tier_bodhisattva
    WinTier.NORMAL_SUPPORTER -> R.drawable.tier_normal_supporter
    WinTier.HALF_AND_HALF -> R.drawable.tier_half_and_half
    WinTier.FAIRY_CANDIDATE -> R.drawable.tier_fairy_candidate
    WinTier.WIN_COLLECTOR -> R.drawable.tier_win_collector
    WinTier.WIN_FAIRY -> R.drawable.tier_win_fairy
    WinTier.WIN_GUARANTEE -> R.drawable.tier_win_guarantee
    WinTier.BASEBALL_GOD -> R.drawable.tier_baseball_god
}