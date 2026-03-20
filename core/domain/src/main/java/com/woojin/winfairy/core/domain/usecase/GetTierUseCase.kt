package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.model.WinTier
import javax.inject.Inject

class GetTierUseCase @Inject constructor() {
    operator fun invoke(winRate: Float, isNoGame: Boolean): WinTier {
        return if (isNoGame) {
            WinTier.NO_GAME
        } else {
            WinTier.entries.lastOrNull { winRate >= it.minWinRate } ?: WinTier.BASEBALL_GOD
        }
    }
}