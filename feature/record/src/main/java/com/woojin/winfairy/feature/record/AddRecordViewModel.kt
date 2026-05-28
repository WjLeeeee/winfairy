package com.woojin.winfairy.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woojin.winfairy.core.domain.usecase.AddGameRecordUseCase
import com.woojin.winfairy.core.domain.usecase.DeleteUpComingGameUseCase
import com.woojin.winfairy.core.domain.usecase.GetDistinctVariableValuesUseCase
import com.woojin.winfairy.core.domain.usecase.GetRecordByIdUseCase
import com.woojin.winfairy.core.domain.usecase.GetUpComingGameByIdUseCase
import com.woojin.winfairy.core.domain.usecase.GetVariablesByRecordIdUseCase
import com.woojin.winfairy.core.domain.usecase.UpdateGameRecordUseCase
import com.woojin.winfairy.core.model.GameRecord
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.GameVariable
import com.woojin.winfairy.core.model.KboTeam
import com.woojin.winfairy.core.model.VariableCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecordViewModel @Inject constructor(
    private val addGameRecordUseCase: AddGameRecordUseCase,
    private val updateGameRecordUseCase: UpdateGameRecordUseCase,
    private val getRecordByIdUseCase: GetRecordByIdUseCase,
    private val getVariablesByRecordIdUseCase: GetVariablesByRecordIdUseCase,
    private val getDistinctVariableValuesUseCase: GetDistinctVariableValuesUseCase,
    private val getUpComingGameByIdUseCase: GetUpComingGameByIdUseCase,
    private val deleteUpComingGameUseCase: DeleteUpComingGameUseCase
) : ViewModel() {

    private val _recordData = MutableStateFlow(RecordData())
    val recordData: StateFlow<RecordData> = _recordData.asStateFlow()

    private val _suggestions = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val suggestions: StateFlow<Map<String, List<String>>> = _suggestions.asStateFlow()

    private val _isMyTeamHome = MutableStateFlow(true)
    val isMyTeamHome: StateFlow<Boolean> = _isMyTeamHome

    private var isEditMode = false
    private var editRecordId: Long = 0
    private var isAddUpComingGame = false
    private var upComingGameId: Long = 0

    fun loadUpComingData(id: Long, isKorean: Boolean, myTeam: KboTeam) {
        viewModelScope.launch {
            val upComingData = getUpComingGameByIdUseCase(id)
            if (upComingData != null) {

                val myTeamStadium = if (isKorean) myTeam.stadium else myTeam.stadiumEn
                val isMyTeamHome = upComingData.stadium == myTeamStadium

                isAddUpComingGame = true
                upComingGameId = id
                _recordData.value = RecordData(
                    selectedDate = upComingData.date,
                    selectedEnemy = KboTeam.entries.find { it.name == upComingData.opponentTeam },
                    selectedStadium = upComingData.stadium,
                    gameResult = GameResult.DRAW,
                    homeScore = 0,
                    awayScore = 0,
                    variables = VariableCategory.entries.map { category ->
                        val categoryName = if (isKorean) category.displayName else category.displayNameEn
                        VariableInput(
                            category = categoryName,
                            value = "",
                            values = emptyList(),
                            isMultiple = category.isMultiple
                        )
                    }
                )

                _isMyTeamHome.value = isMyTeamHome
            }
        }
    }

    fun loadRecord(recordId: Long, isKorean: Boolean) {
        viewModelScope.launch {
            val record = getRecordByIdUseCase(recordId) ?: return@launch
            val variables = getVariablesByRecordIdUseCase(recordId)

            isEditMode = true
            editRecordId = recordId

            _recordData.value = RecordData(
                selectedDate = record.date,
                selectedEnemy = KboTeam.entries.find { it.name == record.opponentTeam },
                selectedStadium = record.stadium,
                gameResult = record.result,
                homeScore = record.homeScore,
                awayScore = record.awayScore,
                variables = VariableCategory.entries.map { category ->
                    val categoryName = if (isKorean) category.displayName else category.displayNameEn
                    val categoryValues = variables.filter { it.category == categoryName }
                    VariableInput(
                        category = categoryName,
                        value = if (!category.isMultiple) categoryValues.firstOrNull()?.value ?: "" else "",
                        values = if (category.isMultiple) categoryValues.map { it.value } else emptyList(),
                        isMultiple = category.isMultiple
                    )
                }
            )
        }
    }

    fun loadSuggestions() {
        viewModelScope.launch {
            val result = mutableMapOf<String, List<String>>()
            VariableCategory.entries.forEach { category ->
                val values = getDistinctVariableValuesUseCase(category.displayName)
                result[category.displayName] = values
                // 영어도
                val valuesEn = getDistinctVariableValuesUseCase(category.displayNameEn)
                result[category.displayNameEn] = valuesEn
            }
            _suggestions.value = result
        }
    }

    fun addVariableValue(index: Int, value: String) {
        _recordData.update { data ->
            data.copy(
                variables = data.variables.toMutableList().apply {
                    val current = this[index]
                    if (value !in current.values) {
                        this[index] = current.copy(values = current.values + value)
                    }
                }
            )
        }
    }

    fun removeVariableValue(index: Int, value: String) {
        _recordData.update { data ->
            data.copy(
                variables = data.variables.toMutableList().apply {
                    val current = this[index]
                    this[index] = current.copy(values = current.values - value)
                }
            )
        }
    }

    fun initVariables(isKorean: Boolean) {
        if (_recordData.value.variables.isEmpty()) {
            _recordData.update {
                it.copy(
                    variables = VariableCategory.entries.map { category ->
                        VariableInput(
                            category = if (isKorean) category.displayName else category.displayNameEn,
                            value = "",
                            isMultiple = category.isMultiple
                        )
                    }
                )
            }
        }
    }

    fun updateRecordData(
        date: String? = null,
        enemy: KboTeam? = null,
        stadium: String? = null,
        gameResult: GameResult? = null,
        homeScore: Int? = null,
        awayScore: Int? = null,
    ) {
        _recordData.update { recordData ->
            recordData.copy(
                selectedDate = date ?: recordData.selectedDate,
                selectedEnemy = enemy ?: recordData.selectedEnemy,
                selectedStadium = stadium ?: recordData.selectedStadium,
                gameResult = gameResult ?: recordData.gameResult,
                homeScore = homeScore ?: recordData.homeScore,
                awayScore = awayScore ?: recordData.awayScore,
            )
        }
    }

    fun updateVariable(index: Int, value: String) {
        _recordData.update { recordData ->
            recordData.copy(
                variables = recordData.variables.toMutableList().apply {
                    this[index] = this[index].copy(value = value)
                }
            )
        }
    }

    fun saveRecord(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val data = _recordData.value
            val record = GameRecord(
                id = if (isEditMode) editRecordId else 0,
                date = data.selectedDate,
                opponentTeam = data.selectedEnemy?.name ?: "",
                stadium = data.selectedStadium,
                result = data.gameResult,
                homeScore = data.homeScore,
                awayScore = data.awayScore,
                memo = ""
            )
            val variables = data.variables.flatMap { variable ->
                if (variable.isMultiple) {
                    variable.values.map { GameVariable(category = variable.category, value = it) }
                } else {
                    if (variable.value.isNotBlank()) {
                        listOf(GameVariable(category = variable.category, value = variable.value))
                    } else emptyList()
                }
            }
            if (isEditMode) {
                updateGameRecordUseCase(record, variables)
            } else {
                addGameRecordUseCase(record, variables)
            }
            if (isAddUpComingGame) {
                //예정 경기 추가 시 예정 경기 리스트 에서 해당 아이템 제거
                deleteUpComingGameUseCase(upComingGameId)
            }

            onSuccess()
        }
    }
}