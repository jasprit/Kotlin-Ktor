package com.example.plugins

import com.example.feature.user.User
import com.example.feature.user.auth.AuthRequest
import com.example.routes.userRoutes
import com.example.util.authValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureRequestValidation() {
    install(RequestValidation) {

        authValidation()
    }
}