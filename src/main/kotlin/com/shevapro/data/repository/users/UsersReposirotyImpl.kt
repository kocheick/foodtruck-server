package com.shevapro.data.repository.users

import com.shevapro.data.models.User
import com.shevapro.data.models.UserDBDTO
import com.shevapro.data.models.Users
import com.shevapro.data.models.UsersPlaces
import com.shevapro.data.repository.db.dbQuery
import org.jetbrains.exposed.sql.*
import java.util.*

class UsersReposirotyImpl : UsersReposiroty {

    override suspend fun createUser(username: String, email: String, passwordHash: String) {
        dbQuery {
            Users.insert { insertStatement ->
                insertStatement[this.id] = UUID.randomUUID()
                insertStatement[this.username] = username
                insertStatement[this.email] = email
                insertStatement[this.passwordHash] = passwordHash
                insertStatement[this.creationDate] = System.currentTimeMillis()
            }
        }
    }

    override suspend fun removeUserById(id: UUID) {
        dbQuery {
            Users.deleteWhere {
                Users.id eq id
            }
        }
    }

    override suspend fun removeUserByEmail(email: String) {
        dbQuery {
            Users.deleteWhere {
                Users.email eq email
            }
        }
    }

    override suspend fun getUserByEmail(email: String): User? = dbQuery {
        Users.select { Users.email eq email }.map { toUserEntity(it) }.singleOrNull()
    }


    override suspend fun getUserById(id: UUID): User? = dbQuery {
        Users.select { Users.id eq id }.map { toUserEntity(it) }.singleOrNull()
    }


    override suspend fun update(id: UUID, email: String, username: String) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[this.email] = email
                it[this.username] = username
            }
        }
    }

    override suspend fun addVoteForUser(userId: UUID, placeId: UUID) = dbQuery {
        val userPlaces = UsersPlaces.select { UsersPlaces.user eq userId }.mapNotNull { it[UsersPlaces.place] }

        if (userPlaces.contains(placeId)) {
            UsersPlaces.deleteWhere {
                UsersPlaces.user eq userId
            }
        } else {
            UsersPlaces.insert {
                it[this.user] = userId
                it[this.place] = placeId
                it[this.timestamp] = System.currentTimeMillis()
            }
        }
        Unit
    }


// get a place  or id
//check if it exists
// checks if users fav list contains id
//if not, add to it, else return

    override suspend fun verifyUser(email: String, password: String): Boolean {
        val user = dbQuery {
            Users.select { Users.email eq email }.map { toUserDTO(it) }.singleOrNull()
        } ?: return false
        return user.passwordHash == password
    }

    override suspend fun getUsers(): List<User> {
        return dbQuery { Users.selectAll().map { toUserEntity(it) }.toList() }
    }

    private fun toUserEntity(row: ResultRow): User {
        val id = row[Users.id]
        val email = row[Users.email]
        val username = row[Users.username]
        val userPlacesIds = getUserPlaces(id)
        return User(id,
            username,
            email,
            userPlacesIds)

    }

    private fun toUserDTO(row: ResultRow): UserDBDTO {
        val id = row[Users.id]
        val email = row[Users.email]
        val username = row[Users.username]
        val userPlaces = getUserPlaces(id)
        val passwordHash = row[Users.passwordHash]
        val list = userPlaces.ifEmpty { emptyList() }

        return UserDBDTO(id, username, email, passwordHash, list)
    }

    private fun getUserPlaces(id: UUID) = UsersPlaces.select { UsersPlaces.user eq id }.mapNotNull {
        it[UsersPlaces.place]
    }

    private fun fromUserDTOtoEntity(user: UserDBDTO): User =
        User(user.id, user.username, user.email, user.favoritePlaces)
}