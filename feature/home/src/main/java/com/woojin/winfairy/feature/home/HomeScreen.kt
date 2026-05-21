package com.woojin.winfairy.feature.home

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.woojin.winfairy.feature.home.achievement.AchievementItem
import com.woojin.winfairy.feature.home.analysis.AnalysisItem
import com.woojin.winfairy.feature.home.components.AddRecordFab
import com.woojin.winfairy.feature.home.record.RecordItem
import com.woojin.winfairy.feature.home.upcomingmatch.AddUpComingMatchBottomSheet
import com.woojin.winfairy.feature.home.upcomingmatch.UpComingMatchItem

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun HomeScreen(
    onComplete: (Int) -> Unit,
    onEditRecord: (Int, Long) -> Unit,
    selectedTeam: KboTeam,
    recordItem: (Long, Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val allRecord by homeViewModel.allRecord.collectAsState()
    val winRate by homeViewModel.winRate.collectAsState()
    val tier by homeViewModel.tier.collectAsState()
    val upComingGameList by homeViewModel.upComingGame.collectAsState()

    val analysisResult by homeViewModel.analysisResult.collectAsState()
    val achievement by homeViewModel.achievements.collectAsState()

    var planVisitBottomSheet by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        floatingActionButton = {
            AddRecordFab { onComplete(allRecord.size + 1) }
        }
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
                selectedTab = selectedTab,
                tabSelected = { index -> selectedTab = index }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 25.dp)
                    .padding(top = if (allRecord.isEmpty()) 20.dp else 0.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                when (selectedTab) {
                    0 -> {
                        UpComingMatchItem(
                            upcomingGames = upComingGameList,
                            myTeam = selectedTeam,
                            onAddClick = { planVisitBottomSheet = true },
                            deleteItem = { id -> homeViewModel.deleteUpComingGame(id) },
                            recordItem = { id -> recordItem(id, allRecord.size) },
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        RecordItem(
                            modifier = Modifier.weight(1f),
                            recordItem = allRecord,
                            onItemClick = { index, recordId -> onEditRecord(index, recordId) },
                            onDelete = { recordId -> homeViewModel.deleteRecord(recordId) },
                            myTeam = selectedTeam,
                        )
                    }

                    1 -> AnalysisItem(
                        modifier = Modifier.weight(1f),
                        gameCount = allRecord.size,
                        analysisResult = analysisResult
                    )

                    2 -> AchievementItem(
                        modifier = Modifier.weight(1f),
                        achievementItem = achievement
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        AddUpComingMatchBottomSheet(
            isShow = planVisitBottomSheet,
            closeBottomSheet = { planVisitBottomSheet = false },
            myTeam = selectedTeam,
            registerBtn = { upcomingGame ->
                homeViewModel.regisUpComingGame(upcomingGame)
                planVisitBottomSheet = false
                Toast.makeText(context, context.getString(R.string.registered), Toast.LENGTH_SHORT)
                    .show()
            },
        )
    }
}

@Composable
fun HeaderLayout(
    modifier: Modifier = Modifier,
    selectedTeam: KboTeam,
    allRecord: List<GameRecord>,
    winRate: Float,
    tier: WinTier,
    selectedTab: Int,
    tabSelected: (Int) -> Unit
) {
    val isKorean = java.util.Locale.getDefault().language == "ko"

    val wins = allRecord.count { it.result == GameResult.WIN }
    val draws = allRecord.count { it.result == GameResult.DRAW }
    val loses = allRecord.count { it.result == GameResult.LOSE }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                //팀명
                Text(
                    text = "${if (isKorean) selectedTeam.teamName else selectedTeam.teamNameEn} ${if (isKorean) selectedTeam.subName else selectedTeam.subNameEn}",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                )
                //승률
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${winRate.toInt()}%",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 36.sp,
                        lineHeight = 36.sp,
                    )
                    Text(
                        text = stringResource(R.string.win_rate),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                    )
                }
                //승무패
                Text(
                    text = stringResource(R.string.win_lose_text, wins, draws, loses),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                )
                //기록이 1경기 이상 있을때 최근 5 경기
                if (allRecord.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.last_five_games),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 13.sp,
                        lineHeight = 13.sp,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    allRecord.take(5).forEach { gameRecord ->
                        RecentGameResultBadge(gameRecord = gameRecord)
                    }
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
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = if (isKorean) tier.tierName else tier.tierNameEn,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    modifier = Modifier.offset(y = -(5).dp)
                )
                Text(
                    text = if (isKorean) tier.description else tier.descriptionEn,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    lineHeight = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y = -(5).dp)
                )
            }
        }
        HomeMainTab(
            selectedTab = selectedTab,
            onTabClick = { index -> tabSelected(index) }
        )
    }
}

@Composable
fun RecentGameResultBadge(gameRecord: GameRecord) {
    val color = when (gameRecord.result) {
        GameResult.WIN -> Color.White
        else -> Color(0xFF888888)
    }
    val text = when (gameRecord.result) {
        GameResult.WIN -> "W"
        GameResult.LOSE -> "L"
        GameResult.DRAW -> "D"
        GameResult.CANCELED -> "C"
    }
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color.Transparent)
            .border(
                width = 2.dp,
                color = color,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 12.sp
        )
    }
}

@Composable
fun HomeMainTab(
    selectedTab: Int,
    onTabClick: (Int) -> Unit,
) {
    val tabs = listOf(
        stringResource(R.string.record),
        stringResource(R.string.analysis),
        stringResource(R.string.achievement)
    )
    SecondaryTabRow(
        modifier = Modifier,
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTab),
                color = Color.White
            )
        },
        divider = {},     // 하단 구분선 제거
        tabs = {
            tabs.forEachIndexed { index, title ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onTabClick(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        lineHeight = 15.sp,
                        color = if (selectedTab == index) Color.White
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        })
}