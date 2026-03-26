package com.woojin.winfairy.feature.home.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woojin.winfairy.core.model.VariableWinRate
import com.woojin.winfairy.feature.home.R

@Composable
fun AnalysisItem(
    modifier: Modifier = Modifier,
    gameCount: Int,
    analysisResult: List<VariableWinRate>
) {
    if (gameCount < 3) {
        NeedMoreGameRecord(
            modifier = modifier,
            gameCount = gameCount
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
                text = stringResource(R.string.overall_win_rate_ranking),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
            AllWinRateRankItem(analysisResult = analysisResult)
        }
    }
}