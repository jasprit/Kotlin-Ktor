package com.example.config

import com.example.routes.userRoutes
import com.example.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

import org.koin.ktor.ext.inject

fun Application.configureRouting(

) {
    // Get dependencies using Koin
    val authServices: AuthService by inject()


    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    // Add our Routes here..
    userRoutes(authServices)
}
