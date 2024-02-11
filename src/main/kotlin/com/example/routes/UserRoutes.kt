package com.example.routes


import com.example.feature.user.UserServices
import com.example.feature.user.User
import com.example.feature.user.auth.AuthRequest
import com.example.feature.user.auth.AuthResponse
import com.example.feature.user.auth.toUser
import com.example.security.HashingService
import com.example.security.SaltedHash
import com.example.token.TokenClaim
import com.example.token.TokenConfig
import com.example.token.TokenService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.codec.digest.DigestUtils


fun Application.userRoutes(
    userServices: UserServices,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    routing {

        route("/users") {

            post("signup") { request ->
                val requestBody = call.receive<AuthRequest>()

                //Check if user is exit before.
                val isUserExits = userServices.findUserByEmail(requestBody.email)

                if (isUserExits != null) {
                    call.respond(HttpStatusCode.BadRequest, "user exists")
                    return@post
                }
                val saltedHash = hashingService.generateSaltedHash(requestBody.password)
                val user =
                    requestBody.toUser().copy(password = saltedHash.hash, salt = saltedHash.salt)

                if (userServices.createUser(user))
                    call.respond(HttpStatusCode.OK)
                else
                    call.respond(HttpStatusCode.BadRequest, requestBody)
            }

            post("login") { request ->
                val requestBody = call.receive<AuthRequest>()

//                val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
//                    call.respond(HttpStatusCode.BadRequest)
//                    return@post
//                }

                val user = userServices.findUserByEmail(requestBody.email)
                if (user == null) {
                    call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                    return@post
                }

                val isValidPassword = hashingService.verify(
                    value = requestBody.password,
                    saltedHash = SaltedHash(
                        hash = user.password,
                        salt = user.salt ?: ""
                    )
                )
                if (!isValidPassword) {
                    println("Entered hash: ${DigestUtils.sha256Hex("${user.salt}${user.password}")}, Hashed PW: ${user.password}")
                    call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                    return@post
                }

                val token = tokenService.generate(
                    config = tokenConfig,
                    TokenClaim(
                        name = "userId",
                        value = user.id.toString()
                    )
                )

                call.respond(
                    status = HttpStatusCode.OK,
                    message = AuthResponse(
                        token = token,
                        name = ""
                    )
                )
            }
        }
    }
}
