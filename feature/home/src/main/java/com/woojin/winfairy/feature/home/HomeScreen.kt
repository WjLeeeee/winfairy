package com.woojin.winfairy.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.KboTeam
import java.util.Locale

@Composable
fun HomeScreen(
    onComplete: () -> Unit,
    selectedTeam: KboTeam,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val allRecord by homeViewModel.allRecord.collectAsState()
    val winRate by homeViewModel.winRate.collectAsState()
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
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .align(Alignment.TopStart)
                        .offset(y = -(18).dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    allRecord.take(5).forEach { gameRecord ->
                        RecentGameResultBadge(gameRecord = gameRecord)
                    }
                }
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
    }
}

@Composable
fun RecentGameResultBadge(gameRecord: GameRecord) {
    val borderColor = when(gameRecord.result) {
        GameResult.WIN -> MaterialTheme.colorScheme.primary
        GameResult.LOSE -> Color(0xFF888888)
        GameResult.DRAW -> Color(0xFFCCCCCC)
        else -> Color.White
    }
    val text = when(gameRecord.result) {
        GameResult.WIN -> "W"
        GameResult.LOSE -> "L"
        GameResult.DRAW -> "D"
        else -> ""
    }
    val textColor = when(gameRecord.result) {
        GameResult.WIN -> MaterialTheme.colorScheme.primary
        GameResult.LOSE -> Color(0xFF888888)
        GameResult.DRAW -> Color(0xFFCCCCCC)
        else -> Color.White
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