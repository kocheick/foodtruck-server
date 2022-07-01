package com.shevapro.data.models

import com.shevapro.UUIDSerializer
import com.shevapro.data.models.UsersPlaces.nullable
import com.shevapro.plugins.UUIDListSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.day
import org.jetbrains.exposed.sql.kotlin.datetime.hour
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID ,
    val username: String,
    val email : String,
    val favoritePlaces : List<@Serializable(with = UUIDSerializer::class)UUID> = emptyList(),
    //val settings : UserSetting?= null,
)


data class UserSetting(val defaultLocation: Coordinates) {

}

object Users: Table() {
    val id = uuid("user_ID")
    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
    val email = varchar("email", 128).uniqueIndex()
    val username = varchar("username", 32)
    val passwordHash = varchar("password_hash", 40)
    val creationDate = long("creation_date")

}

data class UserDBDTO(    val id: UUID ,
                         val username: String,
                         val email : String,
val passwordHash: String, val favoritePlaces: List<UUID> = emptyList()
)

object UsersPlaces : IntIdTable() {
    val user = reference("user_id", Users.id).nullable()
    val place = reference("place_id", Places.id).nullable()
    val timestamp  = long("date")

}

