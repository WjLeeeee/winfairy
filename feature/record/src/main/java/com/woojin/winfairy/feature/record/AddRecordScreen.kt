package com.woojin.winfairy.feature.record

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.KboTeam
import com.woojin.winfairy.core.ui.logoRes
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.ExperimentalTime

@Composable
fun AddRecordScreen(
    onComplete: () -> Unit,
    selectedTeam: KboTeam,
    recordId: Long? = null, // 수정 에서만 사용
    viewModel: AddRecordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isKorean = Locale.getDefault().language == "ko"
    val scrollState = rememberScrollState()

    val recordData by viewModel.recordData.collectAsState()

    LaunchedEffect(Unit) {
        if (recordId != null) {
            viewModel.loadRecord(recordId, isKorean)
        } else {
            viewModel.initVariables(isKorean)
            viewModel.updateRecordData(
                stadium = if (isKorean) selectedTeam.stadium else selectedTeam.stadiumEn
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TopLayout(
                onBackClick = { onComplete() },
                title = if (recordId == null) R.string.add_intuitive_records else R.string.modify_intuitive_records
            )
            AddRecordBase(
                baseTitle = R.string.date
            ) {
                DateLayout(
                    selectedDate = recordData.selectedDate,
                    onDateSelected = { viewModel.updateRecordData(date = it) }
                )
            }
            AddRecordBase(
                baseTitle = R.string.enemy_team
            ) {
                EnemyTeam(
                    myTeam = selectedTeam,
                    selectedEnemyTeam = recordData.selectedEnemy,
                    onSelected = { selectedEnemyTeam -> viewModel.updateRecordData(enemy = selectedEnemyTeam) }
                )
            }
            AddRecordBase(
                baseTitle = R.string.stadium
            ) {
                Stadium(
                    myTeam = selectedTeam,
                    selectedStadium = recordData.selectedStadium,
                    onSelect = { viewModel.updateRecordData(stadium = if (isKorean) it.stadium else it.stadiumEn) }
                )
            }
            AddRecordBase(
                baseTitle = R.string.game_result
            ) {
                GameResultSelector(
                    resultState = recordData.gameResult,
                    onSelect = { viewModel.updateRecordData(gameResult = it) }
                )
            }
            AddRecordBase(
                baseTitle = R.string.record_variables,
                baseSubTitle = R.string.options
            ) {
                VariableRecord(
                    variables = recordData.variables,
                    onVariableChange = { index, value ->
                        viewModel.updateVariable(
                            index = index, value = value
                        )
                    }
                )
            }
            Text(
                text = stringResource(R.string.save_record),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        if (recordData.selectedEnemy == null) {
                            Toast.makeText(context, R.string.choose_enemy_team, Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.saveRecord { onComplete() }
                        }
                    }
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TopLayout(
    onBackClick: () -> Unit,
    title: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = "back",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() }
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = stringResource(title),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun AddRecordBase(
    baseTitle: Int,
    baseSubTitle: Int? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(baseTitle),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            baseSubTitle?.let { subTitle ->
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(subTitle),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun DateLayout(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val date = LocalDate.parse(selectedDate)
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E)", Locale.KOREAN)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable { showDatePicker = true }
            .padding(12.dp, 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = Color(0xFF999999),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = date.format(formatter),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newDate = LocalDate.ofEpochDay(millis / 86400000)
                            onDateSelected(newDate.toString()) // "2026-03-23" 형식
                        }
                        showDatePicker = false
                    }
                ) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
                headline = null,
                showModeToggle = false,
            )
        }
    }
}

@Composable
fun EnemyTeam(
    myTeam: KboTeam,
    selectedEnemyTeam: KboTeam?,
    onSelected: (KboTeam) -> Unit,
) {
    val isKorean = Locale.getDefault().language == "ko"

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        KboTeam.entries
            .filter { it != myTeam }
            .forEach { team ->
                val isSelected = team == selectedEnemyTeam
                Text(
                    text = if (isKorean) team.teamName else team.teamNameEn,
                    fontSize = 12.sp,
                    color = if (isSelected) Color.White else Color(0xFF666666),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else Color.White
                        )
                        .clickable { onSelected(team) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
    }
}

@Composable
fun Stadium(
    myTeam: KboTeam,
    selectedStadium: String,
    onSelect: (KboTeam) -> Unit,
) {
    val isKorean = Locale.getDefault().language == "ko"
    var expanded by remember { mutableStateOf(false) }
    val myTeamStadium = if (isKorean) myTeam.stadium else myTeam.stadiumEn

    var currentSelectedTeamLogo by remember { mutableIntStateOf(myTeam.logoRes()) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .clickable { expanded = true }
                .padding(12.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(currentSelectedTeamLogo),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = selectedStadium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color(0xFF999999),
                modifier = Modifier.size(18.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(0.dp, 4.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 250.dp)
                .background(Color.White)
        ) {
            KboTeam.entries
                .sortedBy { if (isKorean) it.stadium != myTeamStadium else it.stadiumEn != myTeamStadium }
                .forEach { team ->
                    val stadium = if (isKorean) team.stadium else team.stadiumEn
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Image(
                                    painter = painterResource(team.logoRes()),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                )
                                Text(
                                    text = stadium,
                                    fontSize = 14.sp,
                                    color = if (stadium == selectedStadium)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        onClick = {
                            onSelect(team)
                            expanded = false
                            currentSelectedTeamLogo = team.logoRes()
                        }
                    )
                }
        }
    }
}

@Composable
fun GameResultSelector(
    resultState: GameResult,
    onSelect: (GameResult) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GameResult.entries.forEach { result ->
            val isSelected = result == resultState
            val label = when (result) {
                GameResult.WIN -> stringResource(R.string.win)
                GameResult.LOSE -> stringResource(R.string.lose)
                GameResult.DRAW -> stringResource(R.string.draw)
                GameResult.CANCELED -> stringResource(R.string.cancel)
            }
            val icon = when (result) {
                GameResult.WIN -> Icons.Default.Check
                GameResult.LOSE -> Icons.Default.Close
                GameResult.DRAW -> Icons.Default.Remove
                GameResult.CANCELED -> Icons.Default.Clear
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.White
                    )
                    .clickable { onSelect(result) }
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color.White else Color(0xFF888888),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) Color.White else Color(0xFF888888),
                        modifier = Modifier.size(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = if (isSelected) Color.White else Color(0xFF888888)
                )
            }
        }
    }
}
@Composable
fun VariableRecord(
    variables: List<VariableInput>,
    onVariableChange: (Int, String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        variables.forEachIndexed { index, variable ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = variable.category,
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
                BasicTextField(
                    value = variable.value,
                    onValueChange = { onVariableChange(index, it) },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(12.dp, 8.dp),
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (variable.value.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.input_variable_text_field, variable.category),
                                    fontSize = 13.sp,
                                    color = Color(0xFFCCCCCC)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}