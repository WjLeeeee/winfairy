package com.woojin.winfairy.feature.onboarding

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woojin.winfairy.core.designsystem.theme.primaryColor
import com.woojin.winfairy.core.model.KboTeam
import com.woojin.winfairy.core.ui.logoRes
import java.util.Locale

@Composable
fun OnboardingScreen(onComplete: (KboTeam) -> Unit) {
    val isKorean = Locale.getDefault().language == "ko"

    var selectedTeam by remember { mutableStateOf(KboTeam.entries.first()) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(vertical = 40.dp, horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.onboarding_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.onboarding_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(KboTeam.entries) { team ->
                    TeamSelectItem(
                        team = team,
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
    }
}

@Composable
fun TeamSelectItem(
    modifier: Modifier = Modifier,
    team: KboTeam,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val isKorean = Locale.getDefault().language == "ko"
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) team.primaryColor() else MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = team.logoRes()),
            contentDescription = if (isKorean) team.teamName else team.teamNameEn,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
            Text(text = if (isKorean) team.teamName else team.teamNameEn, color = MaterialTheme.colorScheme.onBackground)
            Text(text = if (isKorean) team.subName else team.subNameEn, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(team.primaryColor()),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "✓", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}