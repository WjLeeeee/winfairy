package com.woojin.winfairy.core.data.mapper

import com.woojin.winfairy.core.database.entity.GameRecordEntity
import com.woojin.winfairy.core.database.entity.GameVariableEntity
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameVariable

fun GameRecordEntity.toDomain(): GameRecord = GameRecord(
    id = id,
    date = date,
    opponentTeam = opponentTeam,
    stadium = stadium,
    result = result,
    memo = memo,
)

fun GameRecord.toEntity(): GameRecordEntity = GameRecordEntity(
    id = id,
    date = date,
    opponentTeam = opponentTeam,
    stadium = stadium,
    result = result,
    memo = memo,
)

fun GameVariable.toEntity(gameRecordId: Long): GameVariableEntity = GameVariableEntity(
    gameRecordId = gameRecordId,
    category = category,
    value = value
)
