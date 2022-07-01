package com.shevapro.data.repository.places

import com.shevapro.data.models.*
import com.shevapro.data.places
import com.shevapro.data.repository.db.dbQuery
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class PlacesRepoImpl : PlacesRepository {

    fun initSample() {
        runBlocking {
            places.forEach {
                val byUserWithID = UUID.randomUUID()
                create(
                    it.address.street, it.address.crossStreet,
                    it.address.streetPosition!!.name, it.coordinates.latitude, it.coordinates.longitude, byUserWithID
                )
            }
        }
    }

    override suspend fun create(
        street: String, crossStreet: String,
        streetPosition: String, latitude: Double, longitude: Double, byUserWithID: UUID,
    ) {
        val placeId = UUID.randomUUID()
        val creationDate = System.currentTimeMillis()

        dbQuery {
            Places.insert {
                it[this.id] = placeId
                it[this.street] = street
                it[this.crossStreet] = crossStreet
                it[this.streetPosition] = streetPosition
                it[this.latitude] = latitude
                it[this.longitude] = longitude
                it[this.createdAt] = creationDate
                it[this.modifiedAt] = creationDate
                it[this.byUserWithId] = byUserWithID
            }

        }
        val min = listOf(0,15,30,45)

        DayOfWeek.values().take((1..7).random()).forEach {
            val openHour = LocalTime.of((7..12).random(),min.random())!!
            val closeHour =  LocalTime.of((13..23).random(),min.random())!!
            addBusinessHours(
                placeId, it, openHour, closeHour
            )
        }

    }

    override suspend fun addBusinessHours(
        placeId: UUID, dayOfWeek: DayOfWeek, openHour: LocalTime,
        closHour: LocalTime
    ) {
        dbQuery {
            PlacesHours.insert {
                it[placeid] = placeId
                it[this.dayOfWeek] = dayOfWeek.value
                it[this.openTime] = openHour.toString()
                it[this.closeTime] = closHour.toString()
            }
        }
    }

    override suspend fun updateBusinessHourForPlace(
        placeId: UUID,
        dayOfWeek: DayOfWeek,
        openHour: LocalTime,
        closHour: LocalTime
    ) {
        dbQuery {
            PlacesHours.update({ PlacesHours.placeid eq placeId }) {
                it[this.placeid] = placeId
                it[this.dayOfWeek] = dayOfWeek.value
                it[this.openTime] = openHour.toString()
                it[this.closeTime] = closHour.toString()
            }
        }
    }

    override suspend fun removeById(id: UUID) {
        dbQuery {
            PlacesHours.deleteWhere { PlacesHours.placeid eq id }
            Places.deleteWhere { Places.id eq id }

        }
    }

    override suspend fun update(
        id: UUID, street: String, crossStreet: String,
        streetPosition: String, latitude: Double, longitude: Double,
    ) {
        dbQuery {
            Places.update({ Places.id eq id }) {
                it[this.street] = street
                it[this.crossStreet] = crossStreet
                it[this.streetPosition] = streetPosition
                it[this.latitude] = latitude
                it[this.longitude] = longitude
                it[this.modifiedAt] = System.currentTimeMillis()
            }
        }

    }

    override suspend fun getPlaces(): List<Place> = dbQuery {
        Places.selectAll().map {
            toPlaceEntity(it)
        }
    }


    private fun getVoteCountForPlace(placeId: UUID): Int =
        UsersPlaces.select {
            UsersPlaces.place eq placeId }.count().toInt()


    override suspend fun getPlacesAround(userLocation: Coordinates): List<Place> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaceById(id: UUID): Place? {
        return dbQuery { Places.select { Places.id eq id }.map { toPlaceEntity(it) }.singleOrNull() }
    }

    override suspend fun clearPlaces() {
        dbQuery {
            PlacesHours.deleteAll()
            Places.deleteAll()
        }
    }

    private fun toPlaceEntity(row: ResultRow): Place {
        val id = row[Places.id]
        val votes = getVoteCountForPlace(id)

        val coordinates = Coordinates(row[Places.latitude], row[Places.longitude])

        val street = row[Places.street]
        val crossStreet = row[Places.crossStreet]
        val position = row[Places.streetPosition]?.let { Position.valueOf(it) } ?: Position.N_A
        val address = Address(street, crossStreet, position)
        //  val createdAt = row[Places.createdAt]

        val modifedAt = row[Places.modifiedAt]!!
        val businessHours = PlacesHours.select { PlacesHours.placeid eq id }.map {
                hrow->
            val openTime = hrow[PlacesHours.openTime]
            val closeTime = hrow[PlacesHours.closeTime]

            val day = DayOfWeek.of(hrow[PlacesHours.dayOfWeek])
            val openhours = LocalTime.of(openTime.substringBefore(":").toInt(),openTime.substringAfter(":").toInt())
            val closehours =LocalTime.of(closeTime.substringBefore(":").toInt(),closeTime.substringAfter(":").toInt())

            BusinessHours(day, openhours, closehours)
        }.toList()



        return Place(
            id = id, coordinates = coordinates,
            address = address, numberOfVotes = votes, lastUpdateDate = modifedAt, businessHours
        )

    }
}