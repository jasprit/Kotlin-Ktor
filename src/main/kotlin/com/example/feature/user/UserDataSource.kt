package com.example.feature.user

interface UserDataSource {
	suspend fun insertUser(user: User): Boolean
	suspend fun getUserByEmail(email: String): User?
}