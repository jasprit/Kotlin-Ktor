package com.example.feature.user.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(val token: String) {
}