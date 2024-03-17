package com.example.config

import com.example.util.authValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureRequestValidation() {
    install(RequestValidation) {

        authValidation()
    }
}