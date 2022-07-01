package com.shevapro.services

import com.shevapro.data.repository.users.UsersReposiroty
import com.shevapro.data.repository.usersplaces.UsersPlacesRepository
import java.util.*

class UserService(private val userRepo: UsersReposiroty) {
    val sessionCounter = 0
    suspend fun getListOfUsers() = userRepo.getUsers()
    suspend fun createNewUser(username: String, email: String, passwordHash: String) =
        userRepo.createUser(username, email, passwordHash)

    suspend fun getUserByEmail(email: String) = userRepo.getUserByEmail(email)
    suspend fun removeUserById(id: UUID) = userRepo.removeUserById(id)
    suspend fun removeUserByEmail(email: String) = userRepo.removeUserByEmail(email)
    suspend fun updateUserById(id: UUID , email: String="", username: String="") = userRepo.update(id, email, username)
    suspend fun doesUserWithEmailExist(email: String): Boolean {
       return userRepo.getUserByEmail(email) != null
    }
    suspend fun addVoteForUser(userid: UUID, placeid: UUID) = userRepo.addVoteForUser(userid,placeid)
    suspend fun getUserByID(id: UUID)  = userRepo.getUserById(id)

    suspend fun isValidUser(email: String,password: String): Boolean {
        return userRepo.verifyUser(email,password)
    }

}