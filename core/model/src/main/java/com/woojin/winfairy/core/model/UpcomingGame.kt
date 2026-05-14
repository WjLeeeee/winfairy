package com.woojin.winfairy.core.model

data class UpcomingGame(
    val id: Long = 0,
    val date: String,
    val opponentTeam: String,
    val stadium: String,
)