package com.shevapro.data.models

import kotlinx.serialization.Serializable

@Serializable
sealed class NetworkResult<T>(
) {
    @Serializable

    data class Success<T>(val data: T) : NetworkResult<T>()

    @Serializable
    data class Error(val message: String,val errorCode: Int?= null) : NetworkResult<String>()
}