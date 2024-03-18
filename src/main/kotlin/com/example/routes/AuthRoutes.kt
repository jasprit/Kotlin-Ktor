package com.example.routes


import com.example.features.auth.LoginRequest
import com.example.features.auth.SignUpRequest
import com.example.services.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.userRoutes(
    authService: AuthService,
) {
    routing {
        route("/users") {
            post("signup") {
                val request = call.receive<SignUpRequest>()
                val statusCode = authService.signUp(request)
                call.respond(statusCode)
            }

            post("login") {
                val request = call.receive<LoginRequest>()
                val response = authService.login(request)
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}

