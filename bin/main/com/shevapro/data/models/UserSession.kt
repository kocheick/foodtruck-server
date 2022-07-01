package com.shevapro.data.models

import io.ktor.auth.*
import java.util.*

data class UserSession(val userId:UUID, val count: Int = 0) : Principal
