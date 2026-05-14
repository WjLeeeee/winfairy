package com.woojin.winfairy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woojin.winfairy.core.domain.usecase.AnalyzeAllVariablesUseCase
import com.woojin.winfairy.core.domain.usecase.CheckAchievementsUseCase
import com.woojin.winfairy.core.domain.usecase.DeleteGameRecordUseCase
import com.woojin.winfairy.core.domain.usecase.DeleteUpComingGame
import com.woojin.winfairy.core.domain.usecase.GetAllRecordUseCase
import com.woojin.winfairy.core.domain.usecase.GetAllRecordsWithVariablesUseCase
import com.woojin.winfairy.core.domain.usecase.GetAllUpComingGameUseCase
import com.woojin.winfairy.core.domain.usecase.GetTierUseCase
import com.woojin.winfairy.core.domain.usecase.GetWinRateUseCase
import com.woojin.winfairy.core.domain.usecase.InsertUpComingGame
import com.woojin.winfairy.core.model.AchievementStatus
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.UpcomingGame
import com.woojin.winfairy.core.model.VariableWinRate
import com.woojin.winfairy.core.model.WinTier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllRecord: GetAllRecordUseCase,
    private val getWinRate: GetWinRateUseCase,
    private val getTier: GetTierUseCase,
    private val deleteGameRecord: DeleteGameRecordUseCase,
    private val getAllRecordsWithVariables: GetAllRecordsWithVariablesUseCase,
    private val analyzeAllVariables: AnalyzeAllVariablesUseCase,
    private val checkAchievements: CheckAchievementsUseCase,
    private val getAllUpComingGameUseCase: GetAllUpComingGameUseCase,
    private val insertUpComingGame: InsertUpComingGame,
    private val deleteUpComingGameUseCase: DeleteUpComingGame,
) : ViewModel() {
    private val _allRecord = MutableStateFlow<List<GameRecord>>(emptyList())
    val allRecord = _allRecord.asStateFlow()

    private val _upComingGame = MutableStateFlow<List<UpcomingGame>>(emptyList())
    val upComingGame = _upComingGame.asStateFlow()

    private val _analysisResult = MutableStateFlow<List<VariableWinRate>>(emptyList())
    val analysisResult = _analysisResult.asStateFlow()

    private val _achievements = MutableStateFlow<List<AchievementStatus>>(emptyList())
    val achievements = _achievements.asStateFlow()

    private val _winRate = MutableStateFlow(0f)
    val winRate = _winRate.asStateFlow()

    private val _tier = MutableStateFlow(WinTier.BASEBALL_GOD)
    val tier = _tier.asStateFlow()

    init {
        viewModelScope.launch {
            getAllRecord().collect { records ->
                _allRecord.value = records
                _winRate.value = getWinRate(records)
                _tier.value = getTier(_winRate.value, records.isEmpty())
            }
        }
        viewModelScope.launch {
            getAllUpComingGameUseCase().collect { games ->
                _upComingGame.value = games
            }
        }
        viewModelScope.launch {
            //모든 기록과 변수를 가져와서, 분석 화면 결과에서 사용될 승률 계산
            getAllRecordsWithVariables().collect { recordsWithVars ->
                _analysisResult.value =
                    analyzeAllVariables(
                        records = recordsWithVars,
                        isKorean = Locale.getDefault().language == "ko"
                    )
                _achievements.value =
                    checkAchievements(
                        records = recordsWithVars,
                        tier = _tier.value
                    )
            }
        }
    }

    fun deleteRecord(recordId: Long) {
        viewModelScope.launch {
            deleteGameRecord(recordId)
        }
    }

    /** 직관 예정 경기 등록 */
    fun regisUpComingGame(game: UpcomingGame) {
        viewModelScope.launch {
            insertUpComingGame(game)
        }
    }

    /** 직관 예정 경기 삭제 */
    fun deleteUpComingGame(id: Long) {
        viewModelScope.launch {
            deleteUpComingGameUseCase(id)
        }
    }

    /** 직관 예정 경기 결과를 기록 */
    fun recordItem(id: Long) {

    }
}