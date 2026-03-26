package com.woojin.winfairy.core.model

enum class AchievementCategory(
    val displayNameKo: String,
    val displayNameEn: String
) {
    ATTENDANCE("직관", "Attendance"),
    STREAK("연승", "Streak"),
    TIER("티어", "Tier"),
    SPECIAL("특별", "Special")
}