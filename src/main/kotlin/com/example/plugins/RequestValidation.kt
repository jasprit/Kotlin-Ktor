package com.example.plugins

import com.example.feature.user.User
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureRequestValidation() {
	install(RequestValidation){
		validate<User> { it.validate() }
	}
}