package com.example.plugins

import com.example.feature.user.UserServices
import com.example.routes.userRoutes
import com.example.util.security.HashingService
import com.example.util.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

import org.koin.ktor.ext.inject

fun Application.configureRouting(

) {
    // Get dependencies using Koin
    val userServices: UserServices by inject()
    val hashingService: HashingService by inject()
    val tokenService: TokenService by inject()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    // Add our Routes here..
    userRoutes(userServices, hashingService, tokenService)
}
