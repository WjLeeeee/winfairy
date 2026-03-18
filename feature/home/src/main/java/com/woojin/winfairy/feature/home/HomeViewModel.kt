package com.woojin.winfairy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woojin.winfairy.core.domain.usecase.GetAllRecordUseCase
import com.woojin.winfairy.core.domain.usecase.GetWinRateUseCase
import com.woojin.winfairy.core.model.GameRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllRecord: GetAllRecordUseCase,
    private val getWinRate: GetWinRateUseCase
) : ViewModel() {
    private val _allRecord = MutableStateFlow<List<GameRecord>>(emptyList())
    val allRecord = _allRecord.asStateFlow()

    private val _winRate = MutableStateFlow(0f)
    val winRate = _winRate.asStateFlow()


    init {
        viewModelScope.launch {
            val records = getAllRecord()
            _allRecord.value = records
            _winRate.value = getWinRate(records)
        }
    }
}