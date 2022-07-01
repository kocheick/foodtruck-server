package com.shevapro.data.models

import io.ktor.server.auth.*
import java.util.*

data class UserSession(val userId: UUID, val count: Int = 0) : Principal
