package com.woojin.winfairy.core.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Onboarding

@Serializable
data object Home

@Serializable
data object AddRecord

@Serializable
data class EditRecord(val recordId: Long)