package com.example.features.auth

import com.example.util.validateAll
import io.ktor.server.plugins.requestvalidation.ValidationResult
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val name: String,
    val email: String,
    var password: String,
    val mobile: String
) {
    fun validate(): ValidationResult = validateAll {
        ::name.length(3..20L)
        ::email.isValidEmail(email)
        ::mobile.length(10L..10L)
        ::password.isValidPassword(password)
    }
}


fun AuthRequest.toUser(): User =
    User(name = this.name, email = this.email, password = this.password, mobile = this.mobile)