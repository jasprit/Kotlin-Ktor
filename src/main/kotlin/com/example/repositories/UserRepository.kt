package com.example.repositories

import com.example.features.auth.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

interface UserRepositories {
    suspend fun findUserById(userId: String): User?
    suspend fun findUserByEmail(email: String): User?
    suspend fun createUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
}


class UserRepositoriesImpl(private val users: CoroutineCollection<User>) : UserRepositories {

    override suspend fun findUserById(userId: String): User? = withContext(Dispatchers.IO) {
        return@withContext users.findOneById(userId)
    }

    override suspend fun findUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        return@withContext users.findOne(User::email eq email)
    }

    override suspend fun createUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            users.insertOne(user).wasAcknowledged()
        } catch (e: Exception) {
            // Handle insertion failure, log error, etc.
            return@withContext false
        }
    }

    override suspend fun updateUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = user.id?.let { users.replaceOneById(it, user) }
            result?.wasAcknowledged() ?: false
        } catch (e: Exception) {
            // Handle update failure, log error, etc.
            return@withContext false
        }
    }
}