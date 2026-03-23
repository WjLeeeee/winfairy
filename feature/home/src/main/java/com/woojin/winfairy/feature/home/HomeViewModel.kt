package com.woojin.winfairy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woojin.winfairy.core.domain.usecase.DeleteGameRecordUseCase
import com.woojin.winfairy.core.domain.usecase.GetAllRecordUseCase
import com.woojin.winfairy.core.domain.usecase.GetTierUseCase
import com.woojin.winfairy.core.domain.usecase.GetWinRateUseCase
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.WinTier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllRecord: GetAllRecordUseCase,
    private val getWinRate: GetWinRateUseCase,
    private val getTier: GetTierUseCase,
    private val deleteGameRecord: DeleteGameRecordUseCase,
) : ViewModel() {
    private val _allRecord = MutableStateFlow<List<GameRecord>>(emptyList())
    val allRecord = _allRecord.asStateFlow()

    private val _winRate = MutableStateFlow(0f)
    val winRate = _winRate.asStateFlow()

    private val _tier = MutableStateFlow(WinTier.BASEBALL_GOD)
    val tier = _tier.asStateFlow()

    val tempRecord = listOf(
        GameRecord(
            id = 0,
            date = "2026-03-14",
            opponentTeam = "두산",
            stadium = "잠실",
            result = GameResult.CANCELED,
            memo = ""
        ),
        GameRecord(
            id = 0,
            date = "2026-03-15",
            opponentTeam = "두산",
            stadium = "잠실",
            result = GameResult.CANCELED,
            memo = ""
        ),
        GameRecord(
            id = 0,
            date = "2026-03-16",
            opponentTeam = "삼성",
            stadium = "대구",
            result = GameResult.WIN,
            memo = ""
        ),
        GameRecord(
            id = 0,
            date = "2026-03-17",
            opponentTeam = "삼성",
            stadium = "대구",
            result = GameResult.WIN,
            memo = ""
        ),
        GameRecord(
            id = 0,
            date = "2026-03-18",
            opponentTeam = "NC",
            stadium = "창원",
            result = GameResult.DRAW,
            memo = ""
        ),
        GameRecord(
            id = 0,
            date = "2026-03-19",
            opponentTeam = "LG",
            stadium = "잠실야구장",
            result = GameResult.WIN,
            memo = ""
        ),
        GameRecord(
            id = 0,
            date = "2026-03-20",
            opponentTeam = "한화",
            stadium = "대전볼파크",
            result = GameResult.LOSE,
            memo = ""
        )
    )

    init {
        viewModelScope.launch {
            getAllRecord().collect { records ->
                _allRecord.value = records
                _winRate.value = getWinRate(records)
                _tier.value = getTier(_winRate.value, records.isEmpty())
            }
        }
    }

    fun deleteRecord(recordId: Long) {
        viewModelScope.launch {
            deleteGameRecord(recordId)
        }
    }
}