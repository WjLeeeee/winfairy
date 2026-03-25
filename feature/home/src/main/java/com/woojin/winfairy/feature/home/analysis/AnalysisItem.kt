package com.woojin.winfairy.feature.home.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woojin.winfairy.core.model.VariableWinRate

@Composable
fun AnalysisItem(
    modifier: Modifier = Modifier,
    analysisResult: List<VariableWinRate>
) {
    if (analysisResult.size < 3) {
        NeedMoreGameRecord(
            modifier = modifier,
            gameCount = analysisResult.size
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BestWinRateItem(analysisResult = analysisResult)
            Text(
                text = "전체 승률 랭킹",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AllWinRateRankItem()
        }
    }
}