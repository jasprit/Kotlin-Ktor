package com.example.feature.user

interface UserServices {
	suspend fun findUserById()
	suspend fun findUserByEmail(email:String): User?
	suspend fun createUser(user: User): Boolean
	suspend fun updateUser(): Boolean
}