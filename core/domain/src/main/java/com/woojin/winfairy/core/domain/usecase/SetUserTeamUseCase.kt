package com.woojin.winfairy.core.domain.usecase

import com.woojin.winfairy.core.domain.repository.UserTeamRepository
import com.woojin.winfairy.core.model.KboTeam
import javax.inject.Inject

class SetUserTeamUseCase @Inject constructor(
    private val repository: UserTeamRepository
) {
    suspend operator fun invoke(team: KboTeam) {
        repository.setUserTeam(team)
    }
}