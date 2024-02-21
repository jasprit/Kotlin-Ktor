package com.example.plugins

import com.example.feature.user.UserServicesImpl
import com.example.routes.userRoutes
import com.example.security.HashingService
import com.example.token.JwtTokenService
import com.example.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userServices: UserServicesImpl
) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    // Add our Routes here..
    userRoutes(userServices)
}
