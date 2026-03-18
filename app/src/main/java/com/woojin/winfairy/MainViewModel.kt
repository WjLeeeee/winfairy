package com.woojin.winfairy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woojin.winfairy.core.domain.usecase.GetUserTeamUseCase
import com.woojin.winfairy.core.domain.usecase.SetUserTeamUseCase
import com.woojin.winfairy.core.model.KboTeam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserTeam: GetUserTeamUseCase,
    private val setUserTeam: SetUserTeamUseCase,
) : ViewModel() {

    private val _selectedTeam = MutableStateFlow<KboTeam?>(null)
    val selectedTeam = _selectedTeam.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _selectedTeam.value = getUserTeam()
            _isLoading.value = false
        }
    }

    fun setTeam(team: KboTeam) {
        viewModelScope.launch {
            setUserTeam(team)
            _selectedTeam.value = team
        }
    }

}