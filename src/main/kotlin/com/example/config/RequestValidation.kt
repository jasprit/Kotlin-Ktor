package com.example.config

import com.example.features.auth.AuthRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<AuthRequest> { user ->
            user.validate()
        }
    }
}