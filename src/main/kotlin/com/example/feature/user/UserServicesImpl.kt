package com.example.feature.user


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase


import org.litote.kmongo.eq

class UserServicesImpl(private val users: CoroutineCollection<User>) : UserServices {

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
