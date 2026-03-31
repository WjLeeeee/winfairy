package com.woojin.winfairy.core.model

data class AchievementStatus(
    val achievement: Achievement,
    val currentProgress: Int,
    val isAchieved: Boolean
)