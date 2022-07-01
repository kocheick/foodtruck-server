package com.shevapro

import com.shevapro.data.models.Position
import com.shevapro.data.models.User
import de.nycode.bcrypt.verify
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import java.net.URI
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object Utils {


    fun getPosition(stringName: String) = when (stringName) {
        Position.SOUTH_WEST.name -> Position.SOUTH_WEST
        Position.SOUTH_EAST.name -> Position.SOUTH_EAST
        Position.NORD_EAST.name -> Position.NORD_EAST
        Position.NORD_WEST.name -> Position.NORD_WEST
        else -> Position.N_A
    }

    fun degreeToRadian(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    fun radianToDegree(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    //TODO change secret key btw pwd-hash and db
    private val hashKey = hex(System.getenv("SECRET_KEYY"))

    private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")


    fun hash(password: String): String {
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(hmacKey)
        return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
    }


    fun hashPassword(password: String) = de.nycode.bcrypt.hash(password, 5)


    /**
     * Check if the password matches the User's password
     */
    fun verifyPassword(attempt: String, passwordHash: String) = verify(attempt, passwordHash.toByteArray())

    /**
     * Returns the hashed version of the supplied password
     */


}

fun String.toUUID(): UUID = UUID.fromString(this.replace("\"", ""))

fun String.cleanNumber() = this.replace(",", "").toDouble()

/**
 * Generates a security code using a [hashFunction], a [date], a [user] and an implicit [HttpHeaders.Referrer]
 * to generate tokens to prevent CSRF attacks.
 */
fun ApplicationCall.securityCode(user: User, date: Long) =
    Utils.hash("$date:${user.id}:${request.host()}:${refererHost()}")

/**
 * Verifies that a code generated from [securityCode] is valid for a [date] and a [user] and an implicit [HttpHeaders.Referrer].
 * It should match the generated [securityCode] and also not be older than two hours.
 * Used to prevent CSRF attacks.
 */
fun ApplicationCall.verifyCode(date: Long, user: User, code: String) =
    securityCode(user, date) == code
            && (System.currentTimeMillis() - date).let {
        it > 0 && it < TimeUnit.MILLISECONDS.convert(
            2,
            TimeUnit.HOURS
        )
    }

/**
 * Obtains the [refererHost] from the [HttpHeaders.Referrer] header, to check it to prevent CSRF attacks
 * from other domains.
 */
fun ApplicationCall.refererHost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        return decoder.decodeString().toUUID()
    }

    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: UUID) {
        encoder.encodeString(value.toString())

    }

}

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalTimeInt", PrimitiveKind.INT)

    @OptIn(InternalSerializationApi::class)
    override fun deserialize(decoder: Decoder): LocalTime {
        val hour: Int = decoder.decodeString().substringBefore(":").toInt()
        val min: Int = decoder.decodeString().substringBefore(":").toInt()
        // Always use 0 as a value for alwaysZero property because we decided to do so.
        return LocalTime.of(hour, min)

    }

    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: LocalTime) {
        encoder.encodeString(value.toString())

    }

}

