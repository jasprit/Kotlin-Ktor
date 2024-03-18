package com.example.services

import com.example.features.auth.AuthRequest
import com.example.repositories.UserRepositories
import io.ktor.http.HttpStatusCode
import com.example.features.auth.AuthResponse
import com.example.features.auth.toUser
import com.example.util.ApiError
import kotlinx.serialization.SerializationException


class AuthService(
    private val userRepositories: UserRepositories,
    private val hashingService: HashingService,
    private val tokenService: TokenService,
    private val emailService: EmailService,
    private val jwtConfig: JWTConfig
) {

    suspend fun signUp(request: AuthRequest): HttpStatusCode {
        try {
            //Check if user already exists
            val userExists = userRepositories.findUserByEmail(request.email) != null
            if (userExists) {
                throw ApiError.Conflict("User already exists")
            }

            val saltedHash = hashingService.generateSaltedHash(request.password)
            val user = request.toUser().copy(password = saltedHash.hash, salt = saltedHash.salt)
            userRepositories.createUser(user)

            //Send user registration confirmation here.
            sendRegistrationConfirmation(user.email)

            return HttpStatusCode.OK
        } catch (e: SerializationException) {
            throw ApiError.UnprocessableEntity()
        } catch (t: Throwable) {
            throw ApiError.BadRequest("Failed to sign up: ${t.message}")
        }
    }

    suspend fun login(request: AuthRequest): AuthResponse {
        try {
            val user = userRepositories.findUserByEmail(request.email)
            if (user == null || !hashingService.verify(
                    request.password,
                    SaltedHash(user.password, user.salt ?: "")
                )
            ) {
                throw ApiError.Conflict("Incorrect username or password")
            }

            val token = tokenService.generate(
                config = jwtConfig,
                TokenClaim(name = "userId", value = user.id.toString())
            )

            return AuthResponse(token = token, name = user.name)
        } catch (e: SerializationException) {
            throw ApiError.UnprocessableEntity()
        } catch (t: Throwable) {
            throw ApiError.BadRequest("Failed to log in: ${t.message}")
        }
    }

    private fun sendRegistrationConfirmation(email: String) {
        // Send user registration confirmation email
        emailService.sendEmail("Welcome", "Registration Successful.", email)
    }
}