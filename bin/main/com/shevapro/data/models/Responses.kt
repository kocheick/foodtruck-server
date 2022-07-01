package com.shevapro.data.models

import com.shevapro.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class LoginResponse(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val token: String,
)

@Serializable
data class UserPlacesResponse(
    @Serializable(with = UUIDSerializer::class)
    val user_id: UUID? = null,
    val total: Int,
    val places_id: List<@Serializable(with = UUIDSerializer::class)
    UUID>,
)

@Serializable
data class PlacesResponse(
    val total: Int,
    val places: List<Place>,
)