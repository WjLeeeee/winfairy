package com.woojin.winfairy.feature.home.upcomingmatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import java.util.Date
import java.util.Locale

/**
 * 직관 예정 경기 추가 BottomSheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUpComingMatchBottomSheet(
    isShow: Boolean,
    closeBottomSheet: () -> Unit = {},
    myTeam: KboTeam,
    registerBtn: (UpcomingGame) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val dateSdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    // 오늘 날짜 포함 총 8
    val dateList = remember {
        (0..7).map {
            Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, it)
            }.time
        }
    }
    val opponents = remember { KboTeam.entries.filter { it != myTeam } }
    var upcomingGame by remember {
        mutableStateOf(
            UpcomingGame(
                date = dateSdf.format(dateList.first()),
                opponentTeam = opponents.first().name,
                stadium = myTeam.stadium
            )
        )
    }
    val selectedOpponentTeam = KboTeam.entries.first { it.name == upcomingGame.opponentTeam }

    if (isShow) {
        ModalBottomSheet(
            onDismissRequest = { closeBottomSheet() },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 우리팀 vs 상대팀
                MatchHeader(
                    myTeam = myTeam,
                    opponent = selectedOpponentTeam,
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 날짜 선택
                SectionTitle(stringResource(R.string.date))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(dateList) { date ->
                        DateItem(
                            date = date,
                            isSelected = upcomingGame.date == dateSdf.format(date),
                            onClick = {
                                upcomingGame = upcomingGame.copy(date = dateSdf.format(date))
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 상대팀 선택
                SectionTitle(stringResource(R.string.opponent))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(opponents) { opponent ->
                        OpponentItem(
                            team = opponent,
                            isSelected = upcomingGame.opponentTeam == opponent.name,
                            onClick = {
                                upcomingGame = upcomingGame.copy(
                                    opponentTeam = opponent.name,
                                    stadium = myTeam.stadium
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 구장 선택
                SectionTitle(stringResource(R.string.stadium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 우리팀 구장과 상대팀 구장 리스트
                    val stadiumOptions = listOf(myTeam.stadium, selectedOpponentTeam.stadium)
                    stadiumOptions.forEach { stadium ->
                        StadiumItem(
                            stadium = stadium,
                            isSelected = upcomingGame.stadium == stadium,
                            onClick = {
                                upcomingGame = upcomingGame.copy(stadium = stadium)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 등록 버튼
                Button(
                    onClick = { registerBtn(upcomingGame) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.regis_up_comming_game),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/** 상단 마스코트 및 팀명 */
@Composable
fun MatchHeader(
    myTeam: KboTeam,
    opponent: KboTeam,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        // My Team
        TeamInfo(team = myTeam, isMyTeam = true)

        Spacer(modifier = Modifier.width(20.dp))

        // Center: VS & Date
        Text(
            text = "VS",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.width(20.dp))

        // Opponent Team
        TeamInfo(team = opponent, isMyTeam = false)
    }
}

/** 상단 정보 아이템 */
@Composable
fun TeamInfo(team: KboTeam, isMyTeam: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = team.mascot()),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
        Text(
            text = team.teamName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isMyTeam) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )
}

/** 날짜 아이템 */
@Composable
fun DateItem(
    date: Date,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val daySdf = SimpleDateFormat("E", Locale.KOREAN)
    val dateSdf = SimpleDateFormat("d", Locale.KOREAN)

    Box(
        modifier = Modifier
            .size(width = 56.dp, height = 64.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFF3F3F3))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = daySdf.format(date),
                fontSize = 12.sp,
                color = if (isSelected) Color.White else Color.Gray
            )
            Text(
                text = dateSdf.format(date),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.Black
            )
        }
    }
}

/** 상대팀 아이템 */
@Composable
fun OpponentItem(
    team: KboTeam,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFF3F3F3)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = color,
        modifier = Modifier.height(36.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = team.teamName,
                fontSize = 14.sp,
                color = if (isSelected) Color.White else Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = team.mascot()),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/** 구장 아이템 */
@Composable
fun StadiumItem(
    stadium: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFF3F3F3)
    val contentColor = if (isSelected) Color.White else Color.Black

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        modifier = Modifier.height(36.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stadium,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = contentColor
            )
        }
    }
}

@Composable
@CustomDevicePreviews
fun AddUpComingMatchBottomSheetPreview() {
    WinFairyTheme(team = KboTeam.Ssg) {
        AddUpComingMatchBottomSheet(true, myTeam = KboTeam.Ssg)
    }
}
