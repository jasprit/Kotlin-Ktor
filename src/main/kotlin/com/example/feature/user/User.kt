package com.example.feature.user

import com.example.util.validateAll
import io.ktor.server.plugins.requestvalidation.ValidationResult
import kotlinx.serialization.Serializable


@Serializable
data class User(val name: String, val email: String, val mobile: String, val password: String) {
	fun validate(): ValidationResult = validateAll {
		::name.length(2..20L)
		::email.isValidEmail(email)
		::mobile.length(10L..10L)
		::password.isValidPassword(password)
	}
}
