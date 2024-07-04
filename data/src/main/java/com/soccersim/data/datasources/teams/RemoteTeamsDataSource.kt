package com.soccersim.data.datasources.teams

import com.soccersim.data.models.TeamEntity

/*
* Remote data source is usually used to handle network interactions
* I left this without implementation as there are no provided endpoints to fetch data
* */
class RemoteTeamsDataSource : TeamsDataSource {
    override suspend fun getTeams(): Result<List<TeamEntity>> {
        return Result.failure(Exception("Remote datasource is not implemented yet"))
    }
}