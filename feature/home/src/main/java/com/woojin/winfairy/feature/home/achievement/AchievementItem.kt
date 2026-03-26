package com.woojin.winfairy.feature.home.achievement

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.woojin.winfairy.core.model.GameRecord

@Composable
fun AchievementItem(
    modifier: Modifier = Modifier,
    recordItem: List<GameRecord>
) {
    var showTimeline by remember { mutableStateOf(false) }

    if (showTimeline) {
        AchievementTimeline(
            onBack = { showTimeline = false }
        )
    } else {
        AchievementList(
            modifier = modifier,
            onTimelineClick = { showTimeline = true }
        )
    }
}