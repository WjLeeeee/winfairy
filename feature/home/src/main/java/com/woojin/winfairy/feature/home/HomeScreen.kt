package com.woojin.winfairy.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.KboTeam
import com.woojin.winfairy.core.model.WinTier
import com.woojin.winfairy.core.ui.iconRes
import java.util.Locale

@Composable
fun HomeScreen(
    onComplete: () -> Unit,
    selectedTeam: KboTeam,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val allRecord by homeViewModel.allRecord.collectAsState()
    val winRate by homeViewModel.winRate.collectAsState()
    val tier by homeViewModel.tier.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HeaderLayout(
                selectedTeam = selectedTeam,
                allRecord = allRecord,
                winRate = winRate,
                tier = tier,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 20.dp)
                    .offset(y = -(18).dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                allRecord.take(5).forEach { gameRecord ->
                    RecentGameResultBadge(gameRecord = gameRecord)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 25.dp)
            ) {
                HomeMainTab(
                    selectedTab = selectedTab,
                    onTabClick = { index -> selectedTab = index }
                )
                Spacer(modifier = Modifier.height(12.dp))
                when (selectedTab) {
                    0 -> RecordItem(modifier = Modifier.weight(1f), recordItem = allRecord)
                    1 -> AnalysisItem(modifier = Modifier.weight(1f), recordItem = allRecord)
                    2 -> AchievementItem(modifier = Modifier.weight(1f), recordItem = allRecord)
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.add_records),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onComplete() }
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun HeaderLayout(
    modifier: Modifier = Modifier,
    selectedTeam: KboTeam,
    allRecord: List<GameRecord>,
    winRate: Float,
    tier: WinTier,
) {
    val isKorean = Locale.getDefault().language == "ko"

    val wins = allRecord.count { it.result == GameResult.WIN }
    val draws = allRecord.count { it.result == GameResult.DRAW }
    val loses = allRecord.count { it.result == GameResult.LOSE }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${if (isKorean) selectedTeam.teamName else selectedTeam.teamNameEn} ${if (isKorean) selectedTeam.subName else selectedTeam.subNameEn}",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${winRate.toInt()}%",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 36.sp
                )
                Text(
                    text = stringResource(R.string.win_rate),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            Text(
                text = stringResource(R.string.win_lose_text, wins, draws, loses),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 13.sp
            )
            if (allRecord.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.last_five_games),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 13.sp
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(tier.iconRes()),
                contentDescription = if (isKorean) tier.tierName else tier.tierNameEn,
                modifier = Modifier
                    .size(80.dp)
            )
            Text(
                text = if (isKorean) tier.tierName else tier.tierNameEn,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                fontSize = 13.sp
            )
            Text(
                text = if (isKorean) tier.description else tier.descriptionEn,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                fontSize = 11.sp,
                lineHeight = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RecentGameResultBadge(gameRecord: GameRecord) {
    val borderColor = when(gameRecord.result) {
        GameResult.WIN -> MaterialTheme.colorScheme.primary
        GameResult.LOSE -> Color(0xFF888888)
        GameResult.DRAW -> Color(0xFFCCCCCC)
        GameResult.CANCELED -> Color(0xFFCCCCCC)
    }
    val text = when(gameRecord.result) {
        GameResult.WIN -> "W"
        GameResult.LOSE -> "L"
        GameResult.DRAW -> "D"
        GameResult.CANCELED -> "C"
    }
    val textColor = when(gameRecord.result) {
        GameResult.WIN -> MaterialTheme.colorScheme.primary
        GameResult.LOSE -> Color(0xFF888888)
        GameResult.DRAW -> Color(0xFFCCCCCC)
        GameResult.CANCELED -> Color(0xFFCCCCCC)
    }
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp
        )
    }
}

@Composable
fun HomeMainTab(
    selectedTab: Int,
    onTabClick: (Int) -> Unit,
) {
    val tabs = listOf(stringResource(R.string.record), stringResource(R.string.analysis), stringResource(R.string.achievement))
    SecondaryTabRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(10.dp)),
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = {},   // 기본 인디케이터 제거
        divider = {},     // 하단 구분선 제거
        tabs = {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabClick(index) },
                    modifier = Modifier
                        .background(
                            if (selectedTab == index) MaterialTheme.colorScheme.primary
                            else Color.White
                        ),
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                        )
                    }
                )
            }
        })
}