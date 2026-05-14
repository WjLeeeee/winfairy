package com.woojin.winfairy.core.data.mapper

import com.woojin.winfairy.core.database.entity.UpComingGameEntity
import com.woojin.winfairy.core.model.UpcomingGame

fun UpComingGameEntity.toDomain(): UpcomingGame = UpcomingGame(
    id = id,
    date = date,
    opponentTeam = opponentTeam,
    stadium = stadium
)

fun UpcomingGame.toEntity(): UpComingGameEntity = UpComingGameEntity(
    id = id,
    date = date,
    opponentTeam = opponentTeam,
    stadium = stadium
)