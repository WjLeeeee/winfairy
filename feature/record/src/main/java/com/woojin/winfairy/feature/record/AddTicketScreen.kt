package com.woojin.winfairy.feature.record

import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.KboTeam
import com.woojin.winfairy.core.ui.mascot
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AddTicketScreen(
    onComplete: () -> Unit = {},
    selectedTeam: KboTeam,
    upComingGameId: Long? = null, //예정 경기 등록 시 사용
    recordId: Long? = null, // 수정 에서만 사용
    gameNo: Int,
    viewModel: AddRecordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isKorean = java.util.Locale.getDefault().language == "ko"
    val view = LocalView.current
    var ticketCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var triggerShare by remember { mutableStateOf(false) }
    var triggerSave by remember { mutableStateOf(false) }

    LaunchedEffect(triggerShare) {
        if (triggerShare) {
            ticketCoordinates?.let { coordinates ->
                shareTicket(
                    view = view,
                    coordinates = coordinates,
                    cacheDir = context.cacheDir,
                    packageName = context.packageName,
                )
            }
            triggerShare = false
        }
    }

    LaunchedEffect(triggerSave) {
        if (triggerSave) {
            ticketCoordinates?.let { coordinates ->
                saveTicketToGallery(
                    view = view,
                    coordinates = coordinates,
                    context = context,
                    onSuccess = {
                        Toast.makeText(context, R.string.save_to_gallery, Toast.LENGTH_SHORT).show()
                    },
                    onFailure = {
                        Toast.makeText(context, R.string.save_fail, Toast.LENGTH_SHORT).show()
                    }
                )
            }
            triggerSave = false
        }
    }

    val recordData by viewModel.recordData.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val isMyTeamHome by viewModel.isMyTeamHome.collectAsState()

    var selectedHomeTeam by remember { mutableStateOf<KboTeam?>(selectedTeam) }
    var selectedAwayTeam by remember { mutableStateOf<KboTeam?>(null) }

    val changeHomeTeam: (KboTeam) -> Unit = { team ->
        selectedHomeTeam = team
        if (team != selectedTeam && selectedAwayTeam != selectedTeam) {
            selectedAwayTeam = selectedTeam
        }
        if (team == selectedTeam && selectedAwayTeam == selectedTeam) {
            selectedAwayTeam = null
        }
    }

    val changeAwayTeam: (KboTeam) -> Unit = { team ->
        selectedAwayTeam = team
        if (team != selectedTeam && selectedHomeTeam != selectedTeam) {
            selectedHomeTeam = selectedTeam
        }
        if (team == selectedTeam && selectedHomeTeam == selectedTeam) {
            selectedHomeTeam = null
        }
    }

    LaunchedEffect(Unit) {
        when {
            recordId != null -> viewModel.loadRecord(recordId, isKorean) //편집 모드
            upComingGameId != null -> viewModel.loadUpComingData(
                upComingGameId,
                isKorean,
                selectedTeam
            ) //예정 경기 저장
            else -> {
                viewModel.initVariables(isKorean)
                viewModel.updateRecordData(
                    stadium = if (isKorean) selectedTeam.stadium else selectedTeam.stadiumEn
                )
            }
        }
        viewModel.loadSuggestions()
    }

    // 편집모드일 때 로드된 enemy로 교체
    LaunchedEffect(recordData.selectedEnemy) {
        recordData.selectedEnemy?.let { selectedAwayTeam = it }
    }

    //직관 예정 경기 기록 시, 저장된 상대팀과 홈팀 여부를 가지고 홈,어웨이 설정
    LaunchedEffect(isMyTeamHome, recordData.selectedEnemy) {
        val enemy = recordData.selectedEnemy ?: return@LaunchedEffect
        if (isMyTeamHome) {
            selectedHomeTeam = selectedTeam
            selectedAwayTeam = enemy
        } else {
            selectedHomeTeam = enemy
            selectedAwayTeam = selectedTeam
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xfff1f0eb))
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TopLayout(
                onBackClick = { onComplete() },
                title = if (recordId == null) R.string.add_intuitive_records else R.string.modify_intuitive_records,
                onDownloadClick = { triggerSave = true },
                onShareClick = { triggerShare = true }
            )
            //티켓 영역 캡쳐를 위해 Box로 감사기
            Box(
                modifier = Modifier.onGloballyPositioned {
                    ticketCoordinates = it
                }
            ) {
                MainTicket(
                    selectedHomeTeam = selectedHomeTeam,
                    selectedAwayTeam = selectedAwayTeam,
                    changeHomeTeam = { changeHomeTeam(it) },
                    changeAwayTeam = { changeAwayTeam(it) },
                    onDateSelected = { viewModel.updateRecordData(date = it) },
                    onScoreSelected = { home, away ->
                        val result = when {
                            home > away -> GameResult.WIN
                            home < away -> GameResult.LOSE
                            else -> GameResult.DRAW
                        }
                        viewModel.updateRecordData(
                            homeScore = home,
                            awayScore = away,
                            gameResult = result
                        )
                    },
                    changeScore = { result -> viewModel.updateRecordData(gameResult = result) },
                    recordData = recordData,
                    suggestions = suggestions,
                    onVariableChange = { index, value -> viewModel.updateVariable(index, value) },
                    onVariableAdd = { index, value -> viewModel.addVariableValue(index, value) },
                    onVariableRemove = { index, value -> viewModel.removeVariableValue(index, value) },
                    gameNo = gameNo,
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
                        val enemy =
                            if (selectedHomeTeam == selectedTeam) selectedAwayTeam else selectedHomeTeam
                        viewModel.updateRecordData(
                            enemy = enemy,
                            gameResult = recordData.gameResult,
                            homeScore = recordData.homeScore,
                            awayScore = recordData.awayScore,
                            stadium = if (isKorean) selectedHomeTeam?.stadium else selectedHomeTeam?.stadiumEn,
                        )
                        if (selectedAwayTeam == null || selectedHomeTeam == null) {
                            Toast.makeText(context, R.string.choose_enemy_team, Toast.LENGTH_SHORT)
                                .show()
                            return@clickable
                        }
                        viewModel.saveRecord { onComplete() }
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
    onShareClick: () -> Unit,
    onDownloadClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
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
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Download,
            contentDescription = "download_ticket",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(24.dp)
                .clickable { onDownloadClick() }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "share_ticket",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(24.dp)
                .clickable { onShareClick() }
        )
    }
}

@Composable
fun MainTicket(
    selectedHomeTeam: KboTeam?,
    selectedAwayTeam: KboTeam?,
    changeHomeTeam: (KboTeam) -> Unit,
    changeAwayTeam: (KboTeam) -> Unit,
    onDateSelected: (String) -> Unit,
    onScoreSelected: (Int, Int) -> Unit,
    changeScore: (GameResult) -> Unit,
    recordData: RecordData,
    suggestions: Map<String, List<String>>,
    onVariableChange: (Int, String) -> Unit,
    onVariableAdd: (Int, String) -> Unit,
    onVariableRemove: (Int, String) -> Unit,
    gameNo: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xfffefbf4))
    ) {
        TicketTopInfo(
            selectedHomeTeam = selectedHomeTeam,
            selectedAwayTeam = selectedAwayTeam,
            changeHomeTeam = { changeHomeTeam(it) },
            changeAwayTeam = { changeAwayTeam(it) },
            onDateSelected = { onDateSelected(it) },
            onScoreSelected = { home, away -> onScoreSelected(home, away) },
            changeGameResult = { result -> changeScore(result) },
            recordData = recordData,
            gameNo = gameNo,
        )
        TicketDivider()
        TicketBottomInfo(
            variables = recordData.variables,
            suggestions = suggestions,
            onVariableChange = { index, value -> onVariableChange(index, value) },
            onVariableAdd = { index, value -> onVariableAdd(index, value) },
            onVariableRemove = { index, value -> onVariableRemove(index, value) },
        )
    }
}

@Composable
fun TicketTopInfo(
    selectedHomeTeam: KboTeam?,
    selectedAwayTeam: KboTeam?,
    changeHomeTeam: (KboTeam) -> Unit,
    changeAwayTeam: (KboTeam) -> Unit,
    onDateSelected: (String) -> Unit,
    onScoreSelected: (Int, Int) -> Unit,
    changeGameResult: (GameResult) -> Unit,
    recordData: RecordData,
    gameNo: Int,
) {
    val isKorean = java.util.Locale.getDefault().language == "ko"

    var showScoreDialog by remember { mutableStateOf(false) }
    var showTeamSelectBottomSheet by remember { mutableStateOf(false) }
    var isHome by remember { mutableStateOf(true) } // 팀 선택 바텀시트 최초 선택되어있는 팀

    //달력 DatePicker 관련
    var showDatePicker by remember { mutableStateOf(false) }
    val date = LocalDate.parse(recordData.selectedDate)
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E)", Locale.KOREAN)
    //------------------

    val stampInfo = remember(recordData.homeScore, recordData.awayScore) {
        when {
            recordData.homeScore > recordData.awayScore -> Pair("  *  WIN  *  ", Color(0xffd23838))
            recordData.homeScore < recordData.awayScore -> Pair("  X  LOSE  X  ", Color(0xff3a4658))
            else -> Pair("  =  DRAW  =  ", Color(0xff7a6b2e))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        //티켓 정보
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.ticket_top_info, "%03d".format(gameNo)),
                color = Color(0xff8a7d5e),
                fontSize = 11.sp,
                lineHeight = 11.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "KBO ${date.year}",
                color = Color(0xff8a7d5e),
                fontSize = 11.sp,
                lineHeight = 11.sp,
            )
        }
        //홈팀 vs 원정팀
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isHome = true
                    showTeamSelectBottomSheet = true
                }
            ) {
                //홈팀 이름
                Column {
                    Text(
                        text = "HOME",
                        fontSize = 11.sp,
                        lineHeight = 11.sp,
                        color = Color(0xff9aa3ad),
                    )
                    Text(
                        text = if (selectedHomeTeam != null) {
                            if (isKorean) selectedHomeTeam.teamName else selectedHomeTeam.teamNameEn
                        } else "",
                        fontSize = 22.sp,
                        lineHeight = 22.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                //홈팀 마스코트
                if (selectedHomeTeam != null) {
                    Image(
                        painter = painterResource(id = selectedHomeTeam.mascot()),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEEEEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("?", fontSize = 20.sp, color = Color(0xFFAAAAAA))
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showScoreDialog = true }
            ) {
                Text(
                    text = recordData.homeScore.toString(),
                    fontSize = 30.sp,
                    lineHeight = 30.sp,
                    color = when {
                        recordData.homeScore > recordData.awayScore -> Color.Red
                        else -> Color(0xff9aa3ad)
                    },
                )
                Text(
                    text = ":",
                    fontSize = 30.sp,
                    lineHeight = 30.sp,
                    color = Color.Black,
                )
                Text(
                    text = recordData.awayScore.toString(),
                    fontSize = 30.sp,
                    lineHeight = 30.sp,
                    color = when {
                        recordData.awayScore > recordData.homeScore -> Color.Red
                        else -> Color(0xff9aa3ad)
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isHome = false
                    showTeamSelectBottomSheet = true
                }
            ) {
                //어웨이 팀 마스코트
                if (selectedAwayTeam != null) {
                    Image(
                        painter = painterResource(id = selectedAwayTeam.mascot()),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEEEEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("?", fontSize = 20.sp, color = Color(0xFFAAAAAA))
                    }
                }
                //어웨이 팀 이름
                Column {
                    Text(
                        text = "AWAY",
                        fontSize = 11.sp,
                        lineHeight = 11.sp,
                        color = Color(0xff9aa3ad),
                    )
                    Text(
                        text = if (selectedAwayTeam != null) {
                            if (isKorean) selectedAwayTeam.teamName else selectedAwayTeam.teamNameEn
                        } else stringResource(R.string.select),
                        fontSize = 22.sp,
                        lineHeight = 22.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        //날짜 및 구장
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //날짜
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker = true },
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.date),
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    color = Color(0xff9aa3ad),
                )
                Text(
                    text = date.format(formatter),
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = Color.Black,
                )
            }
            //구장
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text(
                    text = "Stadium",
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    color = Color(0xff9aa3ad),
                )
                Text(
                    text = if (selectedHomeTeam != null) {
                        if (isKorean) selectedHomeTeam.stadium else selectedHomeTeam.stadiumEn
                    } else "-",
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = Color.Black,
                )
            }
        }
        //스탬프
        Row(
            modifier = Modifier
                .padding(start = 30.dp)
                .graphicsLayer { rotationZ = -6f }
                .clip(RoundedCornerShape(8.dp))
                .border(width = 3.dp, color = stampInfo.second, shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                text = stampInfo.first,
                color = stampInfo.second,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            )
        }
    }

    if (showDatePicker) { //날짜 클릭 시 DatePicker 노출
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val today =
                        LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                    return utcTimeMillis <= today
                }
            }
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

    if (showTeamSelectBottomSheet) {
        TeamSelectBottomSheet(
            initialTeam = if (isHome) selectedHomeTeam else selectedAwayTeam,
            onTeamSelected = { team ->
                if (isHome) changeHomeTeam(team) else changeAwayTeam(team)
                showTeamSelectBottomSheet = false
            },
            onDismiss = { showTeamSelectBottomSheet = false }
        )
    }

    if (showScoreDialog) {
        ScorePickerDialog(
            homeTeamName = selectedHomeTeam?.teamName ?: "",
            awayTeamName = selectedAwayTeam?.teamName ?: "",
            initialHomeScore = recordData.homeScore,
            initialAwayScore = recordData.awayScore,
            onConfirm = { home, away ->
                onScoreSelected(home, away)
                showScoreDialog = false
            },
            onDismiss = { showScoreDialog = false }
        )
    }
}

@Composable
fun TicketDivider(modifier: Modifier = Modifier) {
    val screenBgColor = Color(0xfff1f0eb) //화면 배경색

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
    ) {
        val circleRadius = 12.dp.toPx()
        val centerY = size.height / 2

        // 점선
        drawLine(
            color = Color(0xffc8bda3),
            start = Offset(circleRadius, centerY),
            end = Offset(size.width - circleRadius, centerY),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
        )

        // 왼쪽 반원 (오른쪽 절반만)
        drawArc(
            color = screenBgColor,
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(-circleRadius, centerY - circleRadius),
            size = Size(circleRadius * 2, circleRadius * 2)
        )

        // 오른쪽 반원 (왼쪽 절반만)
        drawArc(
            color = screenBgColor,
            startAngle = 90f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(size.width - circleRadius, centerY - circleRadius),
            size = Size(circleRadius * 2, circleRadius * 2)
        )
    }
}

@Composable
fun TicketBottomInfo(
    variables: List<VariableInput>,
    suggestions: Map<String, List<String>>,
    onVariableChange: (Int, String) -> Unit,
    onVariableAdd: (Int, String) -> Unit,
    onVariableRemove: (Int, String) -> Unit,
) {
    var showVariableSheet by remember { mutableStateOf(false) }

    val seat = variables.find {
        it.category == "좌석" || it.category == "Seat"
    }?.value ?: ""
    val partner = variables.find {
        it.category == "동반자" || it.category == "Companion"
    }?.values?.joinToString(", ") ?: ""
    val uniform = variables.find {
        it.category == "유니폼" || it.category == "Uniform"
    }?.value ?: ""
    val food = variables.find {
        it.category == "음식" || it.category == "Food"
    }?.values?.joinToString(", ") ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showVariableSheet = true }
            .heightIn(min = 100.dp)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        //변수 기록
        Text(
            text = stringResource(R.string.ticket_stub_title),
            color = Color(0xff8a7d5e),
            fontSize = 11.sp,
            lineHeight = 11.sp,
        )
        //좌석 및 동반자
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //좌석
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(R.string.seat),
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    color = Color(0xff9aa3ad),
                )
                Text(
                    text = seat.ifEmpty { "-" },
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = Color.Black,
                )
            }
            //동반자
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(R.string.partner),
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    color = Color(0xff9aa3ad),
                )
                Text(
                    text = partner.ifEmpty { "-" },
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = Color.Black,
                )
            }
        }
        //유니폼 및 음식
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //유니폼
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(R.string.uniform),
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    color = Color(0xff9aa3ad),
                )
                Text(
                    text = uniform.ifEmpty { "-" },
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = Color.Black,
                )
            }
            //음식
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(R.string.food),
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    color = Color(0xff9aa3ad),
                )
                Text(
                    text = food.ifEmpty { "-" },
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = Color.Black,
                )
            }
        }
    }

    if (showVariableSheet) {
        VariableRecordBottomSheet(
            variables = variables,
            suggestions = suggestions,
            onVariableChange = { index, value -> onVariableChange(index, value) },
            onVariableAdd = { index, value -> onVariableAdd(index, value) },
            onVariableRemove = { index, value -> onVariableRemove(index, value) },
            onDismiss = { showVariableSheet = false },
            onSave = { showVariableSheet = false }
        )
    }
}