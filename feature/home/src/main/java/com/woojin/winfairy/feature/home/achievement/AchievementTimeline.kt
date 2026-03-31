package com.woojin.winfairy.feature.home.achievement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woojin.winfairy.core.model.Achievement
import com.woojin.winfairy.core.model.AchievementStatus
import com.woojin.winfairy.feature.home.R
import java.util.Locale

@Composable
fun AchievementTimeline(
    onBack: () -> Unit,
    achievementItem: List<AchievementStatus>,
) {
    val scrollState = rememberScrollState()
    val isKorean = Locale.getDefault().language == "ko"
    val primary = MaterialTheme.colorScheme.primary
    val achieved = achievementItem.filter { it.isAchieved }
    val notAchieved = achievementItem.filter { !it.isAchieved }
    val total = Achievement.entries.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "back",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onBack() }
            )
            Text(
                text = stringResource(R.string.achievement_timeline),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // 요약 카드
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "${achieved.size}" to stringResource(R.string.achieved),
                "${notAchieved.size}" to stringResource(R.string.not_achieved),
                "${(achieved.size * 100 / total)}%" to stringResource(R.string.achievement_rate)
            ).forEach { (value, label) ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = value,
                        fontSize = 20.sp,
                        color = if (label == stringResource(R.string.not_achieved)) Color(0xFFCCCCCC) else primary
                    )
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 타임라인
        Column(
            modifier = Modifier
                .padding(start = 12.dp, bottom = 12.dp)
                .verticalScroll(scrollState)
        ) {
            achieved.forEachIndexed { index, status ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    // 타임라인 도트 + 라인
                    Box(modifier = Modifier.width(28.dp)) {
                        if (index < achieved.lastIndex || notAchieved.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .fillMaxHeight()
                                    .background(Color(0xFFEEEEEE))
                                    .align(Alignment.TopCenter)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(primary)
                                .align(Alignment.TopStart)
                                .offset(x = (-7).dp)
                        )
                    }
                    Column(modifier = Modifier.padding(bottom = 22.dp)) {
                        Text(
                            text = status.achievedDate ?: "",
                            fontSize = 10.sp,
                            color = primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = status.achievement.emoji, fontSize = 18.sp)
                            Column {
                                Text(
                                    text = if (isKorean) status.achievement.nameKo else status.achievement.nameEn,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = if (isKorean) status.achievement.descriptionKo else status.achievement.descriptionEn,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // 다음 업적
            if (notAchieved.isNotEmpty()) {
                val next = notAchieved.first()
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.width(28.dp)) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDDDDDD))
                                .align(Alignment.TopStart)
                                .offset(x = (-7).dp)
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.next_achievement),
                            fontSize = 10.sp,
                            color = Color(0xFFCCCCCC)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(12.dp)
                                .alpha(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🔒", fontSize = 18.sp)
                            Column {
                                Text(
                                    text = if (isKorean) next.achievement.nameKo else next.achievement.nameEn,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "${if (isKorean) next.achievement.descriptionKo else next.achievement.descriptionEn} (${next.currentProgress}/${next.achievement.maxProgress})",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}