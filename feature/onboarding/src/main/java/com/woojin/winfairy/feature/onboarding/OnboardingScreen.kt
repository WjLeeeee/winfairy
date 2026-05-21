package com.woojin.winfairy.feature.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.woojin.winfairy.core.designsystem.theme.WinFairyTheme
import com.woojin.winfairy.core.designsystem.theme.primaryColor
import com.woojin.winfairy.core.model.KboTeam
import com.woojin.winfairy.core.ui.mascot
import com.woojin.winfairy.core.ui.preview.CustomDevicePreviews

@Composable
fun OnboardingScreen(onComplete: (KboTeam) -> Unit) {
    val isKorean = java.util.Locale.getDefault().language == "ko"

    var selectedTeam by remember { mutableStateOf(KboTeam.entries.first()) }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 40.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.onboarding_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.onboarding_description),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    letterSpacing = -(0.04).em
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy((-20).dp), // 카드 겹침
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 30.dp, bottom = 20.dp)
                ) {
                    itemsIndexed(KboTeam.entries.toList()) { index, team ->
                        TeamCard(
                            team = team,
                            index = index,
                            isSelected = team == selectedTeam,
                            onClick = { selectedTeam = team }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(selectedTeam.primaryColor())
                        .clickable { onComplete(selectedTeam) }
                ) {
                    Text(
                        text = stringResource(
                            R.string.onboarding_start_btn,
                            if (isKorean) selectedTeam.teamName else selectedTeam.teamNameEn,
                            if (isKorean) selectedTeam.subName else selectedTeam.subNameEn
                        ),
                        modifier = Modifier
                            .align(Alignment.Center),
                        color = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 40.dp, end = 20.dp)
                    .align(Alignment.TopEnd)
            ) {
                Image(
                    painter = painterResource(selectedTeam.mascot()),
                    contentDescription = if (isKorean) selectedTeam.teamName else selectedTeam.teamNameEn,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}

@Composable
fun TeamCard(
    team: KboTeam,
    index: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val isKorean = java.util.Locale.getDefault().language == "ko"
    val primaryColor = team.primaryColor()

    // 애니메이션 값
    val rotation by animateFloatAsState(
        targetValue = when {
            isSelected -> 0f
            index % 2 == 0 -> -2f
            else -> 2f
        },
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "rotation"
    )
    val offsetY by animateDpAsState(
        targetValue = if (isSelected) (-24).dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "offsetY"
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "scale"
    )
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 2.dp,
        label = "elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = offsetY)
            .graphicsLayer {
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = if (isSelected) BorderStroke(2.5.dp, primaryColor) else null
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(primaryColor)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isKorean) "${team.teamName} ${team.subName}" else "${team.teamNameEn} ${team.subNameEn}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                // 선택 체크
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✓", color = primaryColor, fontSize = 12.sp)
                    }
                }
            }

            // 마스코트 이미지
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(team.mascot()),
                    contentDescription = if (isKorean) team.teamName else team.teamNameEn,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@CustomDevicePreviews()
@Composable
fun OnboardingScreenPreview() {
    WinFairyTheme {
        OnboardingScreen {}
    }
}