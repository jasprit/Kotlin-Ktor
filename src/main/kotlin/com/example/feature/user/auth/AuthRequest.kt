package com.example.feature.user.auth

import com.example.feature.user.User
import com.example.security.SaltedHash
import com.example.util.validateAll
import io.ktor.server.plugins.requestvalidation.ValidationResult
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val name: String,
    val email: String,
    val password: String,
    val mobile: String
) {
    fun validate(): ValidationResult = validateAll {
        ::name.length(2..20L)
        ::email.isValidEmail(email)
        ::mobile.length(10L..10L)
        ::password.isValidPassword(password)
    }
}


fun AuthRequest.toUser(): User =
    User(name = this.name, email = this.email, password = this.password, mobile = this.mobile)