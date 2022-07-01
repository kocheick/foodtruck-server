package com.shevapro.data.repository.usersplaces

import com.shevapro.data.models.UsersPlaces
import com.shevapro.data.repository.db.dbQuery
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.*

interface UsersPlacesRepository {
    suspend fun addOneRecord(userId: UUID, placeId: UUID)

    suspend fun getCountForPlace(placeId: UUID): Int

    suspend fun getPlacesForUser(userId: UUID): List<UUID>
}