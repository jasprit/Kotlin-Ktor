package com.example.feature.user

import kotlinx.serialization.Serializable


@Serializable
data class User(val name: String, val email: String, val mobile: String)