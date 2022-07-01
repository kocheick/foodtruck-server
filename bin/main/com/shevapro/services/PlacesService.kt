package com.shevapro.services

import com.shevapro.data.repository.places.PlacesRepository
import kotlinx.datetime.DayOfWeek
import java.time.LocalTime
import java.util.*

class PlacesService(private val placesRepo: PlacesRepository) {

    suspend fun getPlaces() = placesRepo.getPlaces()

    suspend fun clearPlaces() = placesRepo.clearPlaces()

    suspend fun addNewPlace(
        street: String,
        crossStreet: String,
        position: String,
        latitude: Double,
        longitude: Double,
        byUserWithID: UUID
    ) {
        placesRepo.create(street, crossStreet, position, latitude, longitude, byUserWithID)
    }

    suspend fun getPlacesAround(latitude: Double, longitude: Double, distance: Int = 0) {
        placesRepo.getPlaces()
            .filter { it.coordinates.latitude <= latitude + distance.toDouble() && it.coordinates.longitude <= longitude + distance.toDouble() }
    }

    suspend fun updatePlace(
        id: UUID,
        street: String,
        crossStreet: String,
        position: String,
        latitude: Double = Double.NaN,
        longitude: Double = Double.NaN,
    ) = placesRepo.update(id, street, crossStreet, position, latitude, longitude)

    suspend fun updateBusinessHours(
        placeId: UUID, dayOfWeek: DayOfWeek = DayOfWeek.MONDAY, openHour: LocalTime,
        closHour: LocalTime
    ) = placesRepo.addBusinessHours(placeId, dayOfWeek, openHour, closHour)

    suspend fun getPlaceById(id: UUID) = placesRepo.getPlaceById(id)

    suspend fun removePlaceById(id: UUID) = placesRepo.removeById(id)


}