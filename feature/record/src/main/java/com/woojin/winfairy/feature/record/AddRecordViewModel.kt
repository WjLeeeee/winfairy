package com.woojin.winfairy.feature.record

import androidx.lifecycle.ViewModel
import com.woojin.winfairy.core.model.GameResult
import com.woojin.winfairy.core.model.KboTeam
import com.woojin.winfairy.core.model.VariableCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddRecordViewModel @Inject constructor() : ViewModel() {

    private val _recordData = MutableStateFlow(RecordData())
    val recordData: StateFlow<RecordData> = _recordData.asStateFlow()

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
}