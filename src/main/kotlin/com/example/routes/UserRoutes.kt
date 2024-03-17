package com.example.routes


import com.example.feature.user.UserServices
import com.example.feature.user.auth.AuthRequest
import com.example.feature.user.auth.AuthResponse
import com.example.feature.user.auth.toUser
import com.example.util.security.HashingService
import com.example.util.security.SaltedHash
import com.example.util.token.TokenClaim
import com.example.util.token.TokenConfig
import com.example.util.token.TokenService
import com.example.util.ApiError
import com.example.util.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException


fun Application.userRoutes(
    userServices: UserServices,
    hashingService: HashingService,
    tokenService: TokenService
) {

    // Retrieve JWT configuration from environment variables
    val issuer = System.getenv("JWT_ISSUER") ?: error("JWT issuer is not configured")
    val audience = System.getenv("JWT_AUDIENCE") ?: error("JWT audience is not configured")
    val expiresIn = 365L * 24 * 60 * 60 * 1000 // 365 days
    val secret = System.getenv("JWT_SECRET") ?: error("JWT secret is not configured")

    // Initialize token configuration
    val tokenConfig = TokenConfig(issuer, audience, expiresIn, secret)

    routing {
        route("/users") {
            post("signup") {
                try {
                    val request = call.receive<AuthRequest>()

                    //Check if user already exists.
                    if (userServices.findUserByEmail(request.email) != null) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ErrorResponse(101, "User already there")
                        )
                        return@post
                    }

                    //Generate Salted hash for user.
                    val saltedHash = hashingService.generateSaltedHash(request.password)
                    val user =
                        request.toUser().copy(password = saltedHash.hash, salt = saltedHash.salt)

                    //Create user.
                    if (userServices.createUser(user))
                        call.respond(HttpStatusCode.OK)
                    else
                        throw ApiError.ServerError()

                } catch (e: SerializationException) {
                    throw ApiError.ParsingError()
                } catch (t: Throwable) {
                    throw ApiError.BadRequest("Failed to sign up: ${t.message}")
                }
                return@post
            }

            post("login") {
                try {
                    val request = call.receive<AuthRequest>()

                    //Find user by email
                    val user = userServices.findUserByEmail(request.email)
                    if (user == null || !hashingService.verify(
                            request.password,
                            SaltedHash(user.password, user.salt ?: "")
                        )
                    ) {
                        call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                        return@post
                    }

                    //Generate JWT Token
                    val token = tokenService.generate(
                        config = tokenConfig,
                        TokenClaim(
                            name = "userId",
                            value = user.id.toString()
                        )
                    )

                    call.respond(HttpStatusCode.OK, AuthResponse(token = token, name = user.name))
                } catch (e: SerializationException) {
                    throw ApiError.ParsingError()
                } catch (t: Throwable) {
                    throw ApiError.BadRequest("Failed to log in: ${t.message}")
                }
            }
        }
    }
}


