package com.example.util.token

interface TokenService {

    fun generate(config: TokenConfig, vararg claims: TokenClaim): String
}