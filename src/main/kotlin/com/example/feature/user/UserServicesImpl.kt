package com.example.feature.user


import org.litote.kmongo.coroutine.CoroutineDatabase


import org.litote.kmongo.eq

class UserServicesImpl(db: CoroutineDatabase) : UserServices {


    private val users = db.getCollection<User>(collectionName = "users")


    override suspend fun findUserById() {
        TODO("Not yet implemented")
    }

    override suspend fun findUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun createUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun updateUser(): Boolean {
        TODO("Not yet implemented")
    }
}