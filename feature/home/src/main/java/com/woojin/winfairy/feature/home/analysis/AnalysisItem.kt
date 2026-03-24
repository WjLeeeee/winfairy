package com.woojin.winfairy.feature.home.analysis

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.woojin.winfairy.core.model.GameRecord

@Composable
fun AnalysisItem(
    modifier: Modifier = Modifier,
    recordItem: List<GameRecord>
) {
    if (recordItem.size < 3) {
        NeedMoreGameRecord(
            modifier = modifier,
            gameCount = recordItem.size
        )
    }
}