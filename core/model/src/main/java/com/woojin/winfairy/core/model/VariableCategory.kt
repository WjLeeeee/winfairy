package com.woojin.winfairy.core.model

enum class VariableCategory(
    val displayName: String,
    val displayNameEn: String,
    val isMultiple: Boolean,
) {
    COMPANION("동반자", "Companion", true),
    SEAT("좌석", "Seat", false),
    UNIFORM("유니폼", "Uniform", false),
    FOOD("음식", "Food", true),
}