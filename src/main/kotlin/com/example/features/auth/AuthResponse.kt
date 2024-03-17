package com.example.features.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(val token: String, val name: String)