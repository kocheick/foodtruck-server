package com.shevapro.exceptions

import kotlinx.serialization.Serializable

@Serializable
data class InvalidFieldException(override val message: String) : Exception(message)