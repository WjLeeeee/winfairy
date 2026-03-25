package com.woojin.winfairy.core.model

data class VariableWinRate(
    val category: String,
    val value: String,
    val totalGames: Int,
    val wins: Int,
    val winRate: Float
)