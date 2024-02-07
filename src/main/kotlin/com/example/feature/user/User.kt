package com.example.feature.user

import com.example.util.validateAll
import io.ktor.server.plugins.requestvalidation.ValidationResult
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId



data class User(
	@BsonId val id: ObjectId = ObjectId(),
	val name: String,
	val email: String,
	val mobile: String,
	val password: String
) {
	fun validate(): ValidationResult = validateAll {
		::name.length(2..20L)
		::email.isValidEmail(email)
		::mobile.length(10L..10L)
		::password.isValidPassword(password)
	}
}
