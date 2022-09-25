package com.shevapro.services

import com.shevapro.data.repository.users.UsersReposiroty
import java.util.*

class UserService(private val userRepo: UsersReposiroty) {
    private var _sessionCounter = 0
    val sessionCounter = _sessionCounter++
    suspend fun getListOfUsers() = userRepo.getUsers()
    suspend fun createNewUser(username: String, email: String, passwordHash: String) =
        userRepo.createUser(username, email, passwordHash)

    suspend fun getUserByEmail(email: String) = userRepo.getUserByEmail(email)
    suspend fun removeUserById(id: UUID) = userRepo.removeUserById(id)
    suspend fun removeUserByEmail(email: String) = userRepo.removeUserByEmail(email)
    suspend fun updateUserById(id: UUID , email: String="", username: String="", newPasswordHash : String) = userRepo.updateUserInfo(id, email, username,newPasswordHash)
    suspend fun doesUserWithEmailExist(email: String): Boolean {
       return userRepo.getUserByEmail(email) != null
    }
    suspend fun addVoteForUser(userid: UUID, placeid: UUID) = userRepo.addVoteForUser(userid,placeid)
    suspend fun getUserByID(id: UUID)  = userRepo.getUserById(id)

    suspend fun isValidUser(email: String,password: String): Boolean {
        return userRepo.verifyUser(email,password)
    }

}