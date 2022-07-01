package com.shevapro.data.models

import com.shevapro.LocalTimeSerializer
import com.shevapro.UUIDSerializer
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
    val businessHours: List<BusinessHours>

)

@Serializable
data class Address(
    val street: String,
    @SerialName("cross_street") val crossStreet: String,
    @SerialName("street_position") val streetPosition: Position? = null
)

@Serializable
enum class Position {
    NORD_WEST, NORD_EAST, SOUTH_EAST, SOUTH_WEST, N_A
}

@Serializable
data class Coordinates(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@Serializable
data class BusinessHours(val day: DayOfWeek, @SerialName("open_time") val openTime: @Serializable(LocalTimeSerializer::class) LocalTime,@SerialName("close_time") val closeTime: @Serializable(LocalTimeSerializer::class) LocalTime)

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
    val modifiedAt = long("modified_at").nullable()

    //should be separate table to store users who updates place's table
    val byUserWithId = uuid("user_uuid").references(Users.id)

    //val users = reference("user",Users.id)
}


object PlacesHours : IntIdTable() {
    val placeid = reference("place_id", Places.id)
    val dayOfWeek = integer("week_day") // 1-7 representing days in a week
    val openTime = text("open_hour")
    val closeTime = text("close_hour")
}



