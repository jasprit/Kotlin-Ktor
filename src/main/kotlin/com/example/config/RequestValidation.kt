package com.example.config

import com.example.features.auth.SignUpRequest
import com.example.features.auth.LoginRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<SignUpRequest> { signup ->
            signup.validate()
        }

        validate<LoginRequest> { login ->
            login.validate()
        }
    }
}