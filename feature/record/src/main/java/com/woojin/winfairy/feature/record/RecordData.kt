package com.woojin.winfairy.feature.record

import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.KboTeam
import java.time.LocalDate

data class RecordData(
    val selectedDate: String = LocalDate.now().toString(),
    val selectedEnemy: KboTeam? = null,
    val selectedStadium: String = "",
    val gameResult: GameResult = GameResult.WIN,
    val variables: List<VariableInput> = emptyList()
)