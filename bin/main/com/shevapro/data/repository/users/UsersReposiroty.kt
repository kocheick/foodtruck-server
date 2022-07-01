package com.shevapro.data.repository.users

import com.shevapro.data.models.User
import java.util.*

interface UsersReposiroty {
    suspend fun createUser(username:String, email: String, passwordHash: String,)
    suspend fun getUserByEmail(email:String): User?
    suspend fun getUserById(id:UUID) : User?
    suspend fun update(id:UUID,email: String,username: String)
    suspend fun removeUserById(id: UUID)
    suspend fun removeUserByEmail(email: String)
    suspend fun addVoteForUser(userId: UUID, placeId: UUID)
    suspend fun getUsers(): List<User>
    suspend fun verifyUser(email: String, password: String): Boolean

}