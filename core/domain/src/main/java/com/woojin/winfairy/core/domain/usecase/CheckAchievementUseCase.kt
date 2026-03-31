package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.model.Achievement
import com.woojin.winfairy.core.model.AchievementStatus
import com.woojin.winfairy.core.model.GameRecordWithVariables
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.WinTier
import javax.inject.Inject

class CheckAchievementsUseCase @Inject constructor() {
    operator fun invoke(
        records: List<GameRecordWithVariables>,
        tier: WinTier
    ): List<AchievementStatus> {
        val sortedRecords = records.sortedBy { it.record.date }
        val totalGames = records.size
        val wins = records.count { it.record.result == GameResult.WIN }
        val stadiums = records.map { it.record.stadium }.distinct().size
        val enemies = records.filter { it.record.result == GameResult.WIN }
            .map { it.record.opponentTeam }.distinct().size

        // 최대 연승 계산
        var maxStreak = 0
        var currentStreak = 0
        sortedRecords.forEach {
            if (it.record.result == GameResult.WIN) {
                currentStreak++
                maxStreak = maxOf(maxStreak, currentStreak)
            } else if (it.record.result != GameResult.CANCELED) {
                currentStreak = 0
            }
        }

        // 3연패 이상 후 승리 체크
        var hasComeback = false
        var loseStreak = 0
        sortedRecords.forEach {
            when (it.record.result) {
                GameResult.LOSE -> loseStreak++
                GameResult.WIN -> {
                    if (loseStreak >= 3) hasComeback = true
                    loseStreak = 0
                }
                else -> {}
            }
        }

        // 이달의 요정 체크 (한달 3경기 이상 직관 이후 전승)
        val hasPerfectMonth = sortedRecords
            .filter { it.record.result != GameResult.CANCELED }
            .groupBy { it.record.date.substring(0, 7) } // "2026-03"
            .any { (_, monthRecords) ->
                monthRecords.size >= 3 && monthRecords.all { it.record.result == GameResult.WIN }
            }

        return Achievement.entries.map { achievement ->
            val (progress, achieved) = when (achievement) {
                // 직관 횟수
                Achievement.GAME_1 -> totalGames to (totalGames >= 1)
                Achievement.GAME_3 -> totalGames to (totalGames >= 3)
                Achievement.GAME_5 -> totalGames to (totalGames >= 5)
                Achievement.GAME_10 -> totalGames to (totalGames >= 10)
                Achievement.GAME_20 -> totalGames to (totalGames >= 20)
                Achievement.GAME_50 -> totalGames to (totalGames >= 50)

                // 연승
                Achievement.WIN_STREAK_3 -> maxStreak to (maxStreak >= 3)
                Achievement.WIN_STREAK_5 -> maxStreak to (maxStreak >= 5)
                Achievement.WIN_STREAK_7 -> maxStreak to (maxStreak >= 7)
                Achievement.WIN_STREAK_10 -> maxStreak to (maxStreak >= 10)

                // 티어 (10경기 이상 조건)
                Achievement.TIER_FAIRY_CANDIDATE -> (if (totalGames >= 10) 1 else 0) to (totalGames >= 10 && tier >= WinTier.FAIRY_CANDIDATE)
                Achievement.TIER_WIN_FAIRY -> (if (totalGames >= 10) 1 else 0) to (totalGames >= 10 && tier >= WinTier.WIN_FAIRY)
                Achievement.TIER_WIN_GUARANTEE -> (if (totalGames >= 10) 1 else 0) to (totalGames >= 10 && tier >= WinTier.WIN_GUARANTEE)
                Achievement.TIER_BASEBALL_GOD -> (if (totalGames >= 10) 1 else 0) to (totalGames >= 10 && tier == WinTier.BASEBALL_GOD)

                // 구장
                Achievement.STADIUM_3 -> stadiums to (stadiums >= 3)
                Achievement.STADIUM_5 -> stadiums to (stadiums >= 5)
                Achievement.STADIUM_ALL -> stadiums to (stadiums >= 10)

                // 특별
                Achievement.COMEBACK -> (if (hasComeback) 1 else 0) to hasComeback
                Achievement.ALL_ENEMY_WIN -> enemies to (enemies >= 9)
                Achievement.PERFECT_MONTH -> (if (hasPerfectMonth) 1 else 0) to hasPerfectMonth
            }

            AchievementStatus(
                achievement = achievement,
                currentProgress = minOf(progress, achievement.maxProgress),
                isAchieved = achieved
            )
        }
    }
}