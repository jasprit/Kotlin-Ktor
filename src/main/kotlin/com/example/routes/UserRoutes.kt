package com.example.routes


import com.example.feature.user.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.userRoutes() {
	
	routing {
		
		route("/users") {
			
			get("") {}
			
			post<User>("") { request ->
				val userInfo = call.receive<User>()
				call.respond(HttpStatusCode.OK, userInfo)
			}
		}
	}
}