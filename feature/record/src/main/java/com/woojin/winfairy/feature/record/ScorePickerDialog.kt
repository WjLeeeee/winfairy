package com.woojin.winfairy.feature.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun ScorePickerDialog(
    homeTeamName: String,
    awayTeamName: String,
    initialHomeScore: Int,
    initialAwayScore: Int,
    onConfirm: (homeScore: Int, awayScore: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var homeScore by remember { mutableIntStateOf(initialHomeScore) }
    var awayScore by remember { mutableIntStateOf(initialAwayScore) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScrollScorePicker(
                    teamName = homeTeamName,
                    score = homeScore,
                    onScoreChange = { homeScore = it }
                )
                Text(
                    text = ":",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                ScrollScorePicker(
                    teamName = awayTeamName,
                    score = awayScore,
                    onScoreChange = { awayScore = it }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(homeScore, awayScore) }) {
                Text(stringResource(R.string.ok), color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = Color.Gray)
            }
        }
    )
}

@Composable
fun ScrollScorePicker(
    teamName: String,
    score: Int,
    onScoreChange: (Int) -> Unit,
) {
    val scores = (0..30).toList()
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = score)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val index = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset
            val itemHeight = 54.dp.value // 아이템 높이와 동일하게

            val targetIndex = if (offset > itemHeight / 2) {
                (index + 1).coerceAtMost(scores.lastIndex)
            } else {
                index
            }

            onScoreChange(scores[targetIndex])
            coroutineScope.launch {
                listState.animateScrollToItem(targetIndex)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = teamName, fontSize = 12.sp, color = Color.Gray)
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = null,
            tint = if (score < 30) MaterialTheme.colorScheme.primary else Color.LightGray,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable(enabled = score < 30) {
                    val newScore = (score + 1).coerceAtMost(30)
                    onScoreChange(newScore)
                    coroutineScope.launch {
                        listState.animateScrollToItem(newScore)
                    }
                }
        )
        LazyColumn(
            state = listState,
            modifier = Modifier
                .height(54.dp)
                .width(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = true
        ) {
            items(scores) { s ->
                Box(
                    modifier = Modifier
                        .height(54.dp)
                        .width(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "%02d".format(s),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (s == score) Color.Black else Color.LightGray,
                    )
                }
            }
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = if (score > 0) MaterialTheme.colorScheme.primary else Color.LightGray,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable(enabled = score > 0) {
                    val newScore = (score - 1).coerceAtLeast(0)
                    onScoreChange(newScore)
                    coroutineScope.launch {
                        listState.animateScrollToItem(newScore)
                    }
                }
        )
    }
}