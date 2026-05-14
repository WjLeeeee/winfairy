package com.woojin.winfairy.feature.home.upcomingmatch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woojin.winfairy.core.designsystem.theme.WinFairyTheme
import com.woojin.winfairy.core.model.KboTeam
import com.woojin.winfairy.core.model.UpcomingGame
import com.woojin.winfairy.core.ui.mascot
import com.woojin.winfairy.core.ui.preview.CustomDevicePreviews
import com.woojin.winfairy.feature.home.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun UpComingMatchItem(
    modifier: Modifier = Modifier,
    upcomingGames: List<UpcomingGame>,
    myTeam: KboTeam,
    onAddClick: () -> Unit = {},
    deleteItem: (Long) -> Unit = {},
    recordItem: (Long) -> Unit = {},
) {
    var isExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(upcomingGames) {
        isExpanded = upcomingGames.isNotEmpty()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.5.dp,
                color = Color(0xffFAD992),
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color(0xffFFF7EA))
            .padding(14.dp)
    ) {
        // 헤더: 직관 예정 + 개수 + 추가버튼 + 접기/펼치기
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ConfirmationNumber,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.upcoming_game),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "${upcomingGames.size}",
                fontSize = 11.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.weight(1f))
            // + 추가 버튼
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .clickable { onAddClick() }
                    .background(Color(0xffFAD992)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "추가",
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Transparent)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // 접기/펼치기
            Row(
                modifier = Modifier
                    .clickable(
                        enabled = upcomingGames.isNotEmpty(),
                        onClick = { isExpanded = !isExpanded }
                    )
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xffFAD992))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = if (isExpanded) stringResource(R.string.collapse) else stringResource(R.string.expand),
                    fontSize = 11.sp,
                    color = Color(0xFF999999),
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF999999),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // 펼쳐진 상태: 예정 아이템들
        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                upcomingGames.forEach { game ->
                    UpcomingGameItem(
                        game = game,
                        myTeam = myTeam,
                        deleteItem = { id -> deleteItem(id) },
                        recordItem = { id -> recordItem(id) },
                    )
                }
            }
        }
    }
}

@Composable
fun UpcomingGameItem(
    game: UpcomingGame,
    myTeam: KboTeam,
    deleteItem: (Long) -> Unit,
    recordItem: (Long) -> Unit,
) {
    val isKorean = LocalLocale.current.platformLocale.language == "ko"
    var showActionBottomSheet by remember { mutableStateOf(false) }

    val enemyTeam = KboTeam.entries.find { it.name == game.opponentTeam }
    val dDay = remember(game.date) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val targetDate = sdf.parse(game.date)
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val today = calendar.timeInMillis

            calendar.time = targetDate!!
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val target = calendar.timeInMillis

            ((target - today) / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            0
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.5.dp,
                color = Color(0xffFAD992),
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color(0xffFFF9F2))
            .clickable { showActionBottomSheet = true }
            .padding(12.dp)
    ) {
        // 상단: 예정 태그 + 날짜
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.upcoming),
                fontSize = 9.sp,
                color = Color.Black,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xffFAD992))
                    .padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = game.date,
                fontSize = 10.sp,
                lineHeight = 10.sp,
                color = Color(0xFF999999)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (dDay == 0) "D-Day" else "D-$dDay",
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // 내팀 VS 상대팀
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 내 팀
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(myTeam.mascot()),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = if (isKorean) myTeam.teamName else myTeam.teamNameEn,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isKorean) myTeam.subName else myTeam.subNameEn,
                        fontSize = 9.sp,
                        lineHeight = 9.sp,
                        color = Color(0xFF999999)
                    )
                }
            }

            // VS + 구장
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "VS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF999999),
                    lineHeight = 13.sp
                )
                Text(
                    text = game.stadium,
                    fontSize = 10.sp,
                    color = Color(0xFF999999),
                    lineHeight = 10.sp
                )
            }

            // 상대 팀
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (isKorean) enemyTeam?.teamName ?: "" else enemyTeam?.teamNameEn ?: "",
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isKorean) enemyTeam?.subName ?: "" else enemyTeam?.subNameEn ?: "",
                        fontSize = 9.sp,
                        lineHeight = 9.sp,
                        color = Color(0xFF999999)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Image(
                    painter = painterResource(enemyTeam?.mascot() ?: myTeam.mascot()),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }

    UpcomingGameActionBottomSheet(
        isShow = showActionBottomSheet,
        onDismiss = { showActionBottomSheet = false },
        onDelete = {
            showActionBottomSheet = false
            deleteItem(game.id)
        },
        onRecord = {
            showActionBottomSheet = false
            recordItem(game.id)
        },
    )
}

@Composable
@CustomDevicePreviews
fun UpCommingMatchItemPreview() {
    WinFairyTheme {
        UpComingMatchItem(
            upcomingGames = listOf(
//                UpcomingGame(
//                    date = "2026-05-15(금)",
//                    opponentTeam = KboTeam.Lotte,
//                    stadium = "잠실야구장"
//                )
            ),
            myTeam = KboTeam.DooSan
        )
    }
}