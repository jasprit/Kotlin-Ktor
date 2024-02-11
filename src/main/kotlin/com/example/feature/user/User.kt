package com.example.feature.user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.jetbrains.annotations.NotNull
import org.litote.kmongo.Id


data class User(
    @BsonId
    val id: Id<User>? = null,
    val name: String,


    val email: String,
    val mobile: String,
    val password: String,
    val salt: String? = ""
)

