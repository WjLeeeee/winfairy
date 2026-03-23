package com.woojin.winfairy.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woojin.winfairy.core.domain.usecase.AddGameRecordUseCase
import com.woojin.winfairy.core.domain.usecase.GetRecordByIdUseCase
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
) : ViewModel() {

    private val _recordData = MutableStateFlow(RecordData())
    val recordData: StateFlow<RecordData> = _recordData.asStateFlow()

    private var isEditMode = false
    private var editRecordId: Long = 0

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
                variables = if (variables.isNotEmpty()) {
                    variables.map { VariableInput(it.category, it.value) }
                } else {
                    VariableCategory.entries.map {
                        VariableInput(
                            category = if (isKorean) it.displayName else it.displayNameEn,
                            value = ""
                        )
                    }
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
                            value = ""
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
    ) {
        _recordData.update { recordData ->
            recordData.copy(
                selectedDate = date ?: recordData.selectedDate,
                selectedEnemy = enemy ?: recordData.selectedEnemy,
                selectedStadium = stadium ?: recordData.selectedStadium,
                gameResult = gameResult ?: recordData.gameResult
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
                memo = ""
            )
            val variables = data.variables
                .filter { it.value.isNotBlank() }
                .map { GameVariable(category = it.category, value = it.value) }
            if (isEditMode) {
                updateGameRecordUseCase(record, variables)
            } else {
                addGameRecordUseCase(record, variables)
            }
            onSuccess()
        }
    }
}