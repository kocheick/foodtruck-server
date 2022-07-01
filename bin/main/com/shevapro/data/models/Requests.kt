package com.shevapro.data.models

import com.shevapro.exceptions.InvalidFieldException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(private val _email: String? = null, private val _password: String? = null) {

    val email: String by lazy { _email?.trim()?.replace("\"", "") ?: "" }
    val password: String by lazy { _password?.trim()?.replace("\"", "") ?: "" }


    init {
        validInput()
    }

    private fun validInput() {
        when {
            _email == null -> throw InvalidFieldException(""""Email address" field is missing.""")
            _email.isEmpty() || _email.trim()
                .isBlank() -> throw InvalidFieldException(""""Email address" field is empty.""")
            _password == null -> throw InvalidFieldException(""""Password" field is missing.""")
            _password.isEmpty() || _email.trim()
                .isBlank() -> throw InvalidFieldException(""""Password" field is empty.""")
        }

    }
}


@Serializable
data class SignUpRequest(
    @SerialName("email")
    private val _email: String? = null,
    @SerialName("password")
    private val _password: String? = null,
    @SerialName("username")
    private val _username: String? = null,
) {

    init {
        validEmail()
        validPassword()
    }

    val email: String get() = _email?.trim()?.replace("\"", "")!!
    val password: String get() = _password?.trim()?.replace("\"", "")!!
    val username: String get() = _username?.trim()?.replace("\"", "")!!

    private fun validEmail() {
        when {
            _email == null -> throw InvalidFieldException(""""Email" field is missing.""")
            _email.isEmpty() || _email.isBlank() -> throw InvalidFieldException(""""Email" field is empty.""")
        }

    }

    fun validUsername() {
        if (_username == null) throw InvalidFieldException(""""Username" field is missing.""")
        if (_username.isEmpty()) throw InvalidFieldException(""""Username" field is empty.""")
    }

    private fun validPassword() {
        when {
            _password == null -> throw InvalidFieldException(""""Password" field is missing.""")
            _password.isEmpty() || _password.isBlank() -> throw InvalidFieldException(""""Password" field is empty.""")
            _password.trim().length <= 6 -> throw InvalidFieldException(""""Password" must be longer than 6 characteres.""")
        }
    }
}

@Serializable
data class VoteRequest(
    @SerialName("user_id") private val _userId: String? = null,
    @SerialName("place_id") private val _placeId: String? = null,
) {

    val userId: String by lazy { _userId?.trim()?.replace("\"", "") ?: "" }
    val placeId: String by lazy { _placeId?.trim()?.replace("\"", "") ?: "" }

    init {
        santizeIds()
    }

    private fun santizeIds() {
        when {
            _placeId == null -> throw InvalidFieldException(""""place_id" field is missing.""")
            _placeId.isEmpty() || _placeId.trim()
                .isBlank() -> throw InvalidFieldException(""""place_id" field is empty.""")
            _userId == null -> throw InvalidFieldException(""""user_id" field is missing.""")
            _userId.isEmpty() || _userId.trim()
                .isBlank() -> throw InvalidFieldException(""""user_id" field is empty.""")
        }
    }
}
