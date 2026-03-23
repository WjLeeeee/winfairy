package com.woojin.winfairy.core.model

enum class VariableCategory(
    val displayName: String,
    val displayNameEn: String
) {
    COMPANION("동반자", "Companion"),
    SEAT("좌석", "Seat"),
    UNIFORM("유니폼", "Uniform"),
    FOOD("음식", "Food"),
}