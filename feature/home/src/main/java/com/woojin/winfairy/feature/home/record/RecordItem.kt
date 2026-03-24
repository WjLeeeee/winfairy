package com.woojin.winfairy.feature.home.record

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.feature.home.R

@Composable
fun RecordItem(
    modifier: Modifier = Modifier,
    recordItem: List<GameRecord>,
    onItemClick: (Long) -> Unit,
    onDelete: (Long) -> Unit,
) {
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
                .background(Color.White)
        ) {
            items(recordItem) { record ->
                val resultText = when (record.result) {
                    GameResult.WIN -> stringResource(R.string.win)
                    GameResult.LOSE -> stringResource(R.string.lose)
                    GameResult.DRAW -> stringResource(R.string.draw)
                    GameResult.CANCELED -> stringResource(R.string.cancel)
                }
                val resultBgColor = when (record.result) {
                    GameResult.WIN -> MaterialTheme.colorScheme.primaryContainer
                    GameResult.LOSE -> Color(0xFFF0F0F0)
                    GameResult.DRAW -> Color(0xFFF0F0F0)
                    GameResult.CANCELED -> Color(0xFFF0F0F0)
                }
                val resultTextColor = when (record.result) {
                    GameResult.WIN -> MaterialTheme.colorScheme.primary
                    GameResult.LOSE -> Color(0xFF888888)
                    GameResult.DRAW -> Color(0xFFCCCCCC)
                    GameResult.CANCELED -> Color(0xFFCCCCCC)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable (
                            onClick = { onItemClick(record.id) },
                            onLongClick = { showDeleteDialog = record.id }
                        )
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ){
                        Text(
                            text = "vs ${record.opponentTeam}",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${record.date} | ${record.stadium}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(resultBgColor)
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = resultText,
                            color = resultTextColor,
                            fontSize = 12.sp
                        )
                    }
                }
                if (record != recordItem.last()) {
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE),)
                }
            }
        }
    }
    showDeleteDialog?.let { recordId ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text(text = stringResource(R.string.delete_record_dialog_title), color = Color.Black) },
            text = { Text(text = stringResource(R.string.delete_record_dialog_description), color = Color.Black) },
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