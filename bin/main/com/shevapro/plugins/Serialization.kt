package com.shevapro.plugins

import com.shevapro.UUIDSerializer
import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.util.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
//
//    routing {
//        get("/json/kotlinx-serialization") {
//                call.respond(mapOf("hello" to "world"))
//            }
//    }
}

object UUIDListSerializer : KSerializer<List<UUID>> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    private val serializer = ListSerializer(UUIDSerializer)
    override fun deserialize(decoder: Decoder): List<UUID> {
        return serializer.deserialize(decoder)
    }

    override fun serialize(encoder: Encoder, value: List<UUID>) {
        serializer.serialize(encoder, value)
    }
}

object ExceptionSerializer : KSerializer<Exception>{
    override fun deserialize(decoder: Decoder): Exception {
        TODO("Not yet implemented")
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("ExceptionMessage", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Exception) {
        TODO("Not yet implemented")
    }
}
