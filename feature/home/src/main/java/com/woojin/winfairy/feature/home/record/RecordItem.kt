package com.woojin.winfairy.feature.home.record

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.KboTeam
import com.woojin.winfairy.core.ui.mascot
import com.woojin.winfairy.feature.home.R

@Composable
fun RecordItem(
    modifier: Modifier = Modifier,
    recordItem: List<GameRecord>,
    onItemClick: (Int, Long) -> Unit,
    onDelete: (Long) -> Unit,
    myTeam: KboTeam,
) {
    val isKorean = java.util.Locale.getDefault().language == "ko"
    var showDeleteDialog by remember { mutableStateOf<Long?>(null) }

    if (recordItem.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_records),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            itemsIndexed(recordItem) { index, record ->
                val enemyTeam = KboTeam.entries.find { it.name == record.opponentTeam }
                val resultText = when (record.result) {
                    GameResult.WIN -> stringResource(R.string.win)
                    GameResult.LOSE -> stringResource(R.string.lose)
                    GameResult.DRAW -> stringResource(R.string.draw)
                }
                val tagColor = when (record.result) {
                    GameResult.WIN -> MaterialTheme.colorScheme.primary
                    GameResult.LOSE -> Color(0xFF888888)
                    GameResult.DRAW -> Color(0xFFCCCCCC)
                }
                val isMyTeamHome = record.stadium == myTeam.stadium || record.stadium == myTeam.stadiumEn
                val homeTeamMascot = painterResource(if (isMyTeamHome) myTeam.mascot() else enemyTeam?.mascot() ?: myTeam.mascot())
                val awayTeamMascot = painterResource(if (!isMyTeamHome) myTeam.mascot() else enemyTeam?.mascot() ?: myTeam.mascot())
                val homeTeamName = if (isMyTeamHome) {
                    if (isKorean) myTeam.teamName else myTeam.teamNameEn
                } else {
                    if (isKorean) enemyTeam?.teamName else enemyTeam?.teamNameEn
                }
                val homeTeamSubName = if (isMyTeamHome) {
                    if (isKorean) myTeam.subName else myTeam.subNameEn
                } else {
                    if (isKorean) enemyTeam?.subName else enemyTeam?.subNameEn
                }
                val awayTeamName = if (!isMyTeamHome) {
                    if (isKorean) myTeam.teamName else myTeam.teamNameEn
                } else {
                    if (isKorean) enemyTeam?.teamName else enemyTeam?.teamNameEn
                }
                val awayTeamSubName = if (!isMyTeamHome) {
                    if (isKorean) myTeam.subName else myTeam.subNameEn
                } else {
                    if (isKorean) enemyTeam?.subName else enemyTeam?.subNameEn
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .combinedClickable(
                            onClick = { onItemClick(recordItem.size - index, record.id) },
                            onLongClick = { showDeleteDialog = record.id }
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp))
                            .background(tagColor)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${record.date} · $resultText",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                    // 카드 본체
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 27.dp)
                            .clip(RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp))
                            .border(
                                width = if (record.result == GameResult.WIN) 1.dp else 0.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp)
                            )
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = Color(0xff9aa3ad),
                            modifier = Modifier
                                .size(30.dp)
                                .padding(start = 12.dp, top = 6.dp)
                        )
                        Row(
                            modifier = Modifier
                                .padding(vertical = 14.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Home Team
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = homeTeamMascot,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(55.dp)
                                        .alpha(
                                            when (record.result) {
                                                GameResult.LOSE -> 0.4f
                                                else -> 1f
                                            }
                                        )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = homeTeamName ?: "",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (record.result == GameResult.WIN) MaterialTheme.colorScheme.onBackground else Color(
                                            0xFF888888
                                        ),
                                        lineHeight = 14.sp
                                    )
                                    Text(
                                        text = homeTeamSubName ?: "",
                                        fontSize = 11.sp,
                                        color = if (record.result == GameResult.WIN) Color(0xFF999999) else Color(
                                            0xFFBBBBBB
                                        ),
                                        lineHeight = 11.sp
                                    )
                                }
                            }

                            // 점수
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = record.homeScore.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = when {
                                        record.homeScore > record.awayScore -> Color.Red
                                        else -> Color(0xff9aa3ad)
                                    },
                                    lineHeight = 16.sp
                                )
                                Text(
                                    text = ":",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xff9aa3ad),
                                    lineHeight = 16.sp
                                )
                                Text(
                                    text = record.awayScore.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = when {
                                        record.awayScore > record.homeScore -> Color.Red
                                        else -> Color(0xff9aa3ad)
                                    },
                                    lineHeight = 16.sp
                                )
                            }

                            // Away Team
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = awayTeamName ?: "",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (record.result == GameResult.LOSE) MaterialTheme.colorScheme.onBackground else Color(
                                            0xFF888888
                                        ),
                                        lineHeight = 14.sp
                                    )
                                    Text(
                                        text = awayTeamSubName ?: "",
                                        fontSize = 11.sp,
                                        color = if (record.result == GameResult.LOSE) Color(0xFF999999) else Color(
                                            0xFFBBBBBB
                                        ),
                                        lineHeight = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Image(
                                    painter = awayTeamMascot,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(55.dp)
                                        .alpha(
                                            when (record.result) {
                                                GameResult.WIN -> 0.4f
                                                else -> 1f
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    showDeleteDialog?.let { recordId ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = {
                Text(
                    text = stringResource(R.string.delete_record_dialog_title),
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.delete_record_dialog_description),
                    color = Color.Black
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(recordId)
                        showDeleteDialog = null
                    }
                ) {
                    Text(stringResource(R.string.elimination), color = Color(0xFFE24B4A))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text(stringResource(R.string.cancel), color = Color.Black)
                }
            }
        )
    }
}