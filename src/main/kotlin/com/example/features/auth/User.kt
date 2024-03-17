package com.example.features.auth

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import java.time.LocalDateTime


data class User(
    @BsonId
    val id: Id<User>? = null,
    val name: String,
    val role: Role = Role.USER,
    val email: String,
    val mobile: String,
    val password: String,
    val salt: String? = null,
    val isDeleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Role {
        USER, ADMIN
    }
}

