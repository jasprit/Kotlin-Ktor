package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

interface TokenService {
    fun generate(config: TokenConfig, vararg claims: TokenClaim): String
}


class JwtTokenService : TokenService {
    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn)).apply {
                claims.forEach { claim ->
                    withClaim(claim.name, claim.value)
                }
            }.sign(Algorithm.HMAC256(config.secret))
    }
}

data class TokenClaim(val name: String, val value: String)

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)

