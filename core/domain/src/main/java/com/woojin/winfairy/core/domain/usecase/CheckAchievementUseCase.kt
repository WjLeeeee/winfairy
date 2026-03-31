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

        // 직관 횟수별 달성 날짜
        fun gameCountDate(n: Int): String? {
            return if (sortedRecords.size >= n) sortedRecords[n - 1].record.date else null
        }

        // 최대 연승 + 달성 날짜 계산
        var maxStreak = 0
        var currentStreak = 0
        val streakDates = mutableMapOf<Int, String>() // 연승수 to 달성날짜
        sortedRecords.forEach {
            if (it.record.result == GameResult.WIN) {
                currentStreak++
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak
                    listOf(3, 5, 7, 10).forEach { target ->
                        if (currentStreak == target && !streakDates.containsKey(target)) {
                            streakDates[target] = it.record.date
                        }
                    }
                }
            } else if (it.record.result != GameResult.CANCELED) {
                currentStreak = 0
            }
        }

        // 3연패 이상 후 승리
        var hasComeback = false
        var comebackDate: String? = null
        var loseStreak = 0
        sortedRecords.forEach {
            when (it.record.result) {
                GameResult.LOSE -> loseStreak++
                GameResult.WIN -> {
                    if (loseStreak >= 3 && !hasComeback) {
                        hasComeback = true
                        comebackDate = it.record.date
                    }
                    loseStreak = 0
                }
                else -> {}
            }
        }

        // 구장 수 + 달성 날짜
        val visitedStadiums = mutableSetOf<String>()
        val stadiumDates = mutableMapOf<Int, String>()
        sortedRecords.forEach {
            visitedStadiums.add(it.record.stadium)
            listOf(3, 5, 10).forEach { target ->
                if (visitedStadiums.size >= target && !stadiumDates.containsKey(target)) {
                    stadiumDates[target] = it.record.date
                }
            }
        }
        val stadiums = visitedStadiums.size

        // 전팀 킬러
        val beatenEnemies = mutableSetOf<String>()
        var allEnemyDate: String? = null
        sortedRecords.forEach {
            if (it.record.result == GameResult.WIN) {
                beatenEnemies.add(it.record.opponentTeam)
                if (beatenEnemies.size >= 9 && allEnemyDate == null) {
                    allEnemyDate = it.record.date
                }
            }
        }

        // 이달의 요정
        var perfectMonthDate: String? = null
        sortedRecords
            .filter { it.record.result != GameResult.CANCELED }
            .groupBy { it.record.date.substring(0, 7) }
            .forEach { (month, monthRecords) ->
                if (monthRecords.size >= 3 && monthRecords.all { it.record.result == GameResult.WIN } && perfectMonthDate == null) {
                    perfectMonthDate = monthRecords.last().record.date
                }
            }

        // 티어 달성 날짜는 현재 시점 기준
        val today = sortedRecords.lastOrNull()?.record?.date

        return Achievement.entries.map { achievement ->
            val (progress, achieved, date) = when (achievement) {
                Achievement.GAME_1 -> Triple(totalGames, totalGames >= 1, gameCountDate(1))
                Achievement.GAME_3 -> Triple(totalGames, totalGames >= 3, gameCountDate(3))
                Achievement.GAME_5 -> Triple(totalGames, totalGames >= 5, gameCountDate(5))
                Achievement.GAME_10 -> Triple(totalGames, totalGames >= 10, gameCountDate(10))
                Achievement.GAME_20 -> Triple(totalGames, totalGames >= 20, gameCountDate(20))
                Achievement.GAME_50 -> Triple(totalGames, totalGames >= 50, gameCountDate(50))

                Achievement.WIN_STREAK_3 -> Triple(maxStreak, maxStreak >= 3, streakDates[3])
                Achievement.WIN_STREAK_5 -> Triple(maxStreak, maxStreak >= 5, streakDates[5])
                Achievement.WIN_STREAK_7 -> Triple(maxStreak, maxStreak >= 7, streakDates[7])
                Achievement.WIN_STREAK_10 -> Triple(maxStreak, maxStreak >= 10, streakDates[10])

                Achievement.TIER_FAIRY_CANDIDATE -> Triple(if (totalGames >= 10) 1 else 0, totalGames >= 10 && tier >= WinTier.FAIRY_CANDIDATE, today)
                Achievement.TIER_WIN_FAIRY -> Triple(if (totalGames >= 10) 1 else 0, totalGames >= 10 && tier >= WinTier.WIN_FAIRY, today)
                Achievement.TIER_WIN_GUARANTEE -> Triple(if (totalGames >= 10) 1 else 0, totalGames >= 10 && tier >= WinTier.WIN_GUARANTEE, today)
                Achievement.TIER_BASEBALL_GOD -> Triple(if (totalGames >= 10) 1 else 0, totalGames >= 10 && tier == WinTier.BASEBALL_GOD, today)

                Achievement.STADIUM_3 -> Triple(stadiums, stadiums >= 3, stadiumDates[3])
                Achievement.STADIUM_5 -> Triple(stadiums, stadiums >= 5, stadiumDates[5])
                Achievement.STADIUM_ALL -> Triple(stadiums, stadiums >= 10, stadiumDates[10])

                Achievement.COMEBACK -> Triple(if (hasComeback) 1 else 0, hasComeback, comebackDate)
                Achievement.ALL_ENEMY_WIN -> Triple(beatenEnemies.size, beatenEnemies.size >= 9, allEnemyDate)
                Achievement.PERFECT_MONTH -> Triple(if (perfectMonthDate != null) 1 else 0, perfectMonthDate != null, perfectMonthDate)
            }

            AchievementStatus(
                achievement = achievement,
                currentProgress = minOf(progress, achievement.maxProgress),
                isAchieved = achieved,
                achievedDate = if (achieved) date else null
            )
        }
    }
}