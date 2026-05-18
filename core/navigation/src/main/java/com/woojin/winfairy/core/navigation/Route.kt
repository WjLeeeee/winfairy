package com.woojin.winfairy.core.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Onboarding

@Serializable
data object Home

@Serializable
data class AddRecord(val gameNo: Int)

@Serializable
data class UpComingGameRecord(val gameNo: Int, val id: Long? = null)

@Serializable
data class EditRecord(val gameNo: Int, val recordId: Long)