package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.model.GameRecordWithVariables
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.VariableWinRate
import javax.inject.Inject

class AnalyzeAllVariablesUseCase @Inject constructor() {
    operator fun invoke(records: List<GameRecordWithVariables>): List<VariableWinRate> {
        val allPairs = mutableListOf<Pair<String, Pair<String, GameResult>>>()

        records.forEach { recordWithVars ->
            val result = recordWithVars.record.result
            if (result == GameResult.CANCELED) return@forEach

            // 상대팀
            allPairs.add("상대팀" to (recordWithVars.record.opponentTeam to result))
            // 구장
            allPairs.add("구장" to (recordWithVars.record.stadium to result))
            // 변수들
            recordWithVars.variables.forEach { variable ->
                if (variable.value.isNotBlank()) {
                    allPairs.add(variable.category to (variable.value to result))
                }
            }
        }

        return allPairs
            .groupBy { "${it.first}:${it.second.first}" }
            .map { (key, games) ->
                val (category, value) = key.split(":", limit = 2)
                val total = games.size
                val wins = games.count { it.second.second == GameResult.WIN }
                VariableWinRate(
                    category = category,
                    value = value,
                    totalGames = total,
                    wins = wins,
                    winRate = if (total > 0) wins.toFloat() / total * 100 else 0f
                )
            }
            .sortedWith(compareByDescending<VariableWinRate> { it.winRate }.thenByDescending { it.totalGames })
    }
}