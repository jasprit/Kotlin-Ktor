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
    private val tokenService: TokenService
) {

    suspend fun signUp(request: AuthRequest): HttpStatusCode {
        try {
            val userExists = userRepositories.findUserByEmail(request.email) != null
            if (userExists) {
                throw ApiError.Conflict("User already exists")
            }

            val saltedHash = hashingService.generateSaltedHash(request.password)
            val user = request.toUser().copy(password = saltedHash.hash, salt = saltedHash.salt)
            userRepositories.createUser(user)
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
                config = getTokenConfig(),
                TokenClaim(name = "userId", value = user.id.toString())
            )

            return AuthResponse(token = token, name = user.name)
        } catch (e: SerializationException) {
            throw ApiError.UnprocessableEntity()
        } catch (t: Throwable) {
            throw ApiError.BadRequest("Failed to log in: ${t.message}")
        }
    }

    private fun getTokenConfig(): TokenConfig {
        val issuer = System.getenv("JWT_ISSUER") ?: error("JWT issuer is not configured")
        val audience = System.getenv("JWT_AUDIENCE") ?: error("JWT audience is not configured")
        val expiresIn = 365L * 24 * 60 * 60 * 1000 // 365 days
        val secret = System.getenv("JWT_SECRET") ?: error("JWT secret is not configured")
        return TokenConfig(issuer, audience, expiresIn, secret)
    }
}