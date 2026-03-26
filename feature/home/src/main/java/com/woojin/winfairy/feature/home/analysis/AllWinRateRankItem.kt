package com.woojin.winfairy.feature.home.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woojin.winfairy.core.model.VariableWinRate
import com.woojin.winfairy.feature.home.R

@Composable
fun AllWinRateRankItem(
    modifier: Modifier = Modifier,
    analysisResult: List<VariableWinRate>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
    ) {
        itemsIndexed(analysisResult) { index, item ->
            val primary = MaterialTheme.colorScheme.primary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 순위 뱃지
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == 0) primary
                            else MaterialTheme.colorScheme.primaryContainer
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 11.sp,
                        color = if (index == 0) Color.White else primary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                // 변수 정보
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.value,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 13.sp
                    )
                    Text(
                        text = stringResource(R.string.category_game_count, item.category, item.totalGames),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 10.sp
                    )
                }
                // 미니 프로그레스바 + 승률
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFEEEEEE))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(item.winRate / 100f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (item.winRate >= 50f) primary else Color(0xFFCCCCCC)
                            )
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${item.winRate.toInt()}%",
                    fontSize = 13.sp,
                    color = if (item.winRate >= 50f) primary else Color(0xFF888888),
                    textAlign = TextAlign.End
                )
            }
            if (index < analysisResult.lastIndex) {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color(0xFFEEEEEE),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}