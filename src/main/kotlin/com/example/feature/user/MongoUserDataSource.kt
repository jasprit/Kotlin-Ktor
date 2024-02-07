package com.example.feature.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(db: CoroutineDatabase) : UserDataSource {
	
	private val users = db.getCollection<User>()
	override suspend fun insertUser(user: User): Boolean {
		return users.insertOne(user).wasAcknowledged()
	}
	
	override suspend fun getUserByEmail(email: String): User? {
		return users.findOne(User::email eq email)
	}
}