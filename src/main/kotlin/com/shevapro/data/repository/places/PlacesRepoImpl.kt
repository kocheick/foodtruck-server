package com.shevapro.data.repository.places

import com.shevapro.data.models.*
import com.shevapro.data.repository.db.dbQuery
import kotlinx.datetime.DayOfWeek
import org.jetbrains.exposed.sql.*
import java.time.LocalTime
import java.util.*

class PlacesRepoImpl : PlacesRepository {

    fun initSample() {
//        runBlocking {
//            places.forEach {
//                val byUserWithID = UUID.randomUUID()
//                create(
//                    it.address.street, it.address.crossStreet,
//                    it.address.streetPosition!!.name, it.coordinates.latitude, it.coordinates.longitude, byUserWithID
//                )
//            }
//        }
    }

    override suspend fun create(
        street: String, crossStreet: String,
        streetPosition: String, latitude: Double, longitude: Double, hours: List<BusinessHours>, byUserWithID: UUID,
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
                it[this.createdByUserID] = byUserWithID
                it[this.modifiedByUserID] = byUserWithID
            }
        }
        addHours(placeId, hours)

    }

    private suspend fun addHours(placeId: UUID, hours: List<BusinessHours>) {
        hours.forEach {
            addHourItem(placeId, it.day, it.openingTime, it.closingTime, it.isClosed)
        }
    }

    private suspend fun addHourItem(
        placeId: UUID, dayOfWeek: DayOfWeek, openHour: LocalTime,
        closHour: LocalTime,
        isClosed: Boolean
    ) {
        dbQuery {
            PlacesHours.insert {
                it[this.placeId] = placeId
                it[this.dayOfWeek] = dayOfWeek.value
                it[this.openingTime] = openHour.toString()
                it[this.closingTime] = closHour.toString()
                it[this.isClosed] = isClosed
            }
        }
    }

    private suspend fun updateBusinessHourForPlace(
        placeId: UUID,
        dayOfWeek: DayOfWeek,
        openingTime: LocalTime,
        closingTime: LocalTime,
        isClosed: Boolean
    ) {

        //if item exist, will update otherwise add new item
        val id = dbQuery {
            PlacesHours.select { PlacesHours.placeId eq placeId }.andWhere {  PlacesHours.dayOfWeek eq dayOfWeek.value}.singleOrNull()
               ?.get(PlacesHours.id) }
            if (id != null){
                dbQuery {
                    PlacesHours.update(({ PlacesHours.id eq id })) {
                        it[PlacesHours.dayOfWeek] = dayOfWeek.value
                        it[this.openingTime] = openingTime.toString()
                        it[this.closingTime] = closingTime.toString()
                        it[this.isClosed] = isClosed
                    }
                }

            } else {
                addHourItem(placeId,dayOfWeek,openingTime,closingTime,isClosed)
            }




    }

    override suspend fun removeById(id: UUID) {
        dbQuery {
            PlacesHours.deleteWhere { PlacesHours.placeId eq id }
            UsersPlaces.deleteWhere { UsersPlaces.place eq id }
            Places.deleteWhere { Places.id eq id }

        }
    }

    override suspend fun update(
        id: UUID, street: String, crossStreet: String,
        streetPosition: String, latitude: Double, longitude: Double, hours: List<BusinessHours>, byUserWithID: UUID
    ) {
        dbQuery {
            Places.update({ Places.id eq id }) {
                it[this.street] = street
                it[this.crossStreet] = crossStreet
                it[this.streetPosition] = streetPosition
                it[this.latitude] = latitude
                it[this.longitude] = longitude
                it[this.modifiedAt] = System.currentTimeMillis()
                it[this.modifiedByUserID ] = byUserWithID
            }

        }
        hours.forEach {
            updateBusinessHourForPlace(id,it.day,it.openingTime,it.closingTime,it.isClosed)
        }

    }

    override suspend fun getPlaces(): List<Place> = dbQuery {
        Places.selectAll().map {
            toPlaceEntity(it)
        }
    }


    private fun getVoteCountForPlace(placeId: UUID): Int =
        UsersPlaces.select {
            UsersPlaces.place eq placeId
        }.count().toInt()


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
        val businessHours = PlacesHours.select { PlacesHours.placeId eq id }.map { hrow ->
            val openTime = hrow[PlacesHours.openingTime]
            val closeTime = hrow[PlacesHours.closingTime]
            val isClosed = hrow[PlacesHours.isClosed]

            val day = DayOfWeek.of(hrow[PlacesHours.dayOfWeek])
            val openhours = LocalTime.of(openTime.substringBefore(":").toInt(), openTime.substringAfter(":").toInt())
            val closehours = LocalTime.of(closeTime.substringBefore(":").toInt(), closeTime.substringAfter(":").toInt())

            BusinessHours(day, openhours, closehours, isClosed)
        }.toList().sortedBy { it.day }



        return Place(
            id = id, coordinates = coordinates,
            address = address, numberOfVotes = votes, lastUpdateDate = modifedAt, businessHours
        )

    }
}

