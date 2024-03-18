package com.example.features.auth

import com.example.util.validateAll
import io.ktor.server.plugins.requestvalidation.ValidationResult
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    var password: String
) {
    fun validate(): ValidationResult = validateAll {
        ::email.isValidEmail(email)
        ::password.isValidPassword(password)
    }
}
