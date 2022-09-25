package com.shevapro.data.models

import com.shevapro.LocalTimeSerializer
import com.shevapro.UUIDSerializer
import com.shevapro.plugins.PositionSerializer
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import java.time.LocalTime
import java.util.*

// DTO Model
@Serializable
data class Place(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val coordinates: Coordinates,
    val address: Address,
    @SerialName("number_of_votes") val numberOfVotes: Int = 0,
    val lastUpdateDate: Long,
    @SerialName("business_hours")val businessHours: List<BusinessHours>

)

@Serializable
data class Address(
    val street: String,
    @SerialName("cross_street") val crossStreet: String,
    @SerialName("street_position") val streetPosition: Position? = null
)

@Serializable(with = PositionSerializer::class)
@SerialName("street_position")
enum class Position {
//    nord_west, nord_east, south_east, south_west, n_a
    NORD_WEST, NORD_EAST, SOUTH_EAST, SOUTH_WEST, N_A
}

@Serializable
data class Coordinates(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@Serializable
data class BusinessHours(val day: DayOfWeek, @SerialName("opening_time") val openingTime: @Serializable(LocalTimeSerializer::class) LocalTime, @SerialName("closing_time") val closingTime: @Serializable(LocalTimeSerializer::class) LocalTime, @SerialName("is_closed")val isClosed : Boolean ){
}

// DB Model
object Places : Table() {
    val id = uuid("place_id")
    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
    val streetPosition = varchar("street_position", 15).nullable()
    val street = varchar("street", 50)
    val crossStreet = varchar("cross_street", 50)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val createdAt = long("created_at")
//    val createdby = uuid("created_by_user").references(Users.id)
    val modifiedByUserID = uuid("modified_by_user").references(Users.id)
    val modifiedAt = long("modified_at").nullable()

    //should be separate table to store users who updates place's table
    val createdByUserID = uuid("created_by_user").references(Users.id)

    //val users = reference("user",Users.id)
}


object PlacesHours : IntIdTable() {
    val placeId = reference("place_id", Places.id)
    val dayOfWeek = integer("week_day") // 1-7 representing days in a week
    val openingTime = text("opening_hour")
    val closingTime = text("closing_hour")
    val isClosed = bool("is_closed")
}
@Serializable
data class PlaceDTO(
    val id: String? =null,
    val street: String? = null,
    @SerialName("cross_street") val crossStreet: String?=null,
    @SerialName("street_position") val streetPosition: Position? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("business_hours")val businessHours: List<BusinessHours> = emptyList(),
    val byUserId : String
)


