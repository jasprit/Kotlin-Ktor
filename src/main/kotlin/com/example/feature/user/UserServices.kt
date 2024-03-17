package com.example.feature.user

interface UserServices {
    suspend fun findUserById(userId: String):User?
    suspend fun findUserByEmail(email: String): User?
    suspend fun createUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
}