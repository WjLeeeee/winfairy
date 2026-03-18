package com.woojin.winfairy.core.data.mapper

import com.woojin.winfairy.core.database.entity.GameRecordEntity
import com.woojin.winfairy.core.model.GameRecord

fun GameRecordEntity.toDomain(): GameRecord = GameRecord(
    id = id,
    date = date,
    opponentTeam = opponentTeam,
    stadium = stadium,
    result = result,
    memo = memo,
    createdAt = createAt
)