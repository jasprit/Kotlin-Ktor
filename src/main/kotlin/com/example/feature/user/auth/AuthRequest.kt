package com.example.feature.user.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
	val name: String,
	val email: String,
	val password: String,
	val mobile: String
)