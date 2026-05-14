package com.woojin.winfairy.feature.home.upcomingmatch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woojin.winfairy.feature.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingGameActionBottomSheet(
    isShow: Boolean,
    onDismiss: () -> Unit,
    onRecord: () -> Unit,
    onDelete: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (isShow) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 40.dp)
            ) {
                ActionItem(
                    iconRes = Icons.Default.Edit,
                    iconBg = Color(0xFFEAF3DE),
                    iconTint = Color(0xFF3B6D11),
                    title = stringResource(R.string.up_coming_game_record),
                    desc = stringResource(R.string.up_coming_game_record_content),
                    onClick = onRecord
                )

                HorizontalDivider(color = Color(0xFFF3F3F3))

                ActionItem(
                    iconRes = Icons.Default.Delete,
                    iconBg = Color(0xFFFCEBEB),
                    iconTint = Color(0xFFA32D2D),
                    title = stringResource(R.string.up_coming_game_delete),
                    desc = stringResource(R.string.up_coming_game_delete_content),
                    onClick = onDelete
                )
            }
        }
    }
}

@Composable
private fun ActionItem(
    iconRes: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    desc: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconRes,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Column {
            Text(
                text = title,
                fontSize = 15.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = desc,
                fontSize = 13.sp,
                lineHeight = 13.sp,
                color = Color.Gray
            )
        }
    }
}