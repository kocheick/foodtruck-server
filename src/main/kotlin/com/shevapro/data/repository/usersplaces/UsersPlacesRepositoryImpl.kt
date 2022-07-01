package com.shevapro.data.repository.usersplaces

import com.shevapro.data.models.UsersPlaces
import com.shevapro.data.repository.db.dbQuery
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.*

class UsersPlacesRepositoryImpl : UsersPlacesRepository {


    override suspend fun getCountForPlace(placeId: UUID): Int = dbQuery {
        UsersPlaces.select { UsersPlaces.place eq placeId }.count().toInt()
    }

    override suspend fun getPlacesForUser(userId: UUID): List<UUID> = dbQuery {
        UsersPlaces.select { UsersPlaces.user eq userId }.mapNotNull { it[UsersPlaces.place] }
    }
    override suspend fun addOneRecord(userId: UUID, placeId: UUID) = dbQuery {
        UsersPlaces.insert {
            it[user] = userId
            it[place] = placeId
        }
        Unit
    }
}

