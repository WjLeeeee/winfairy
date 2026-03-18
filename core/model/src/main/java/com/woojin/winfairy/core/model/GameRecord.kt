package com.woojin.winfairy.core.model

data class GameRecord(
    val id: Long = 0,
    val date: String,
    val opponentTeam: String,
    val stadium: String,
    val result: GameResult,
    val memo: String? = null,
    val createdAt: Long = 0
)