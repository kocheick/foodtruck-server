package com.shevapro.data.repository.places

import com.shevapro.data.models.BusinessHours
import com.shevapro.data.models.Coordinates
import com.shevapro.data.models.Place
import kotlinx.datetime.DayOfWeek
import java.time.LocalTime
import java.util.*

interface PlacesRepository {
    suspend fun create(
        street: String, crossStreet: String,
        streetPosition: String, latitude: Double, longitude: Double, hours : List<BusinessHours>,byUserWithID: UUID,
    )
//    suspend fun addBusinessHour(
//        placeId: UUID, dayOfWeek: DayOfWeek, openHour: LocalTime,
//        closHour: LocalTime,
//        isClosed: Boolean
//    )
//    suspend fun updateBusinessHourForPlace(placeId: UUID, dayOfWeek: DayOfWeek, openingTime: LocalTime,
//                                           closingTime: LocalTime, isClosed: Boolean)
    suspend fun removeById(id: UUID)
    suspend fun update(
        id: UUID, street: String, crossStreet: String,
        streetPosition: String, latitude: Double, longitude: Double, hours: List<BusinessHours>,byUserWithID: UUID
    )

    suspend fun getPlaces(): List<Place>
    suspend fun getPlacesAround(userLocation: Coordinates): List<Place>
    suspend fun getPlaceById(id: UUID): Place?
    suspend fun clearPlaces()
}