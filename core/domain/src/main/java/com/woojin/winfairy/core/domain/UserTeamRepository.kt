package com.woojin.winfairy.core.domain

import com.woojin.winfairy.core.model.KboTeam

interface UserTeamRepository {
    suspend fun getUserTeam(): KboTeam?
    suspend fun setUserTeam(team: KboTeam)
}