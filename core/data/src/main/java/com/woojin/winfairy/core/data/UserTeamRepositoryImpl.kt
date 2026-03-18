package com.woojin.winfairy.core.data

import com.woojin.winfairy.core.database.dao.UserTeamDao
import com.woojin.winfairy.core.database.entity.UserTeamEntity
import com.woojin.winfairy.core.domain.repository.UserTeamRepository
import com.woojin.winfairy.core.model.KboTeam
import javax.inject.Inject

class UserTeamRepositoryImpl @Inject constructor(
    private val userTeamDao: UserTeamDao
) : UserTeamRepository {
    override suspend fun getUserTeam(): KboTeam? {
        val entity = userTeamDao.getUserTeam()
        return entity?.let { KboTeam.valueOf(it.teamCode) }
    }

    override suspend fun setUserTeam(team: KboTeam) {
        userTeamDao.setUserTeam(UserTeamEntity(teamCode = team.name))
    }
}