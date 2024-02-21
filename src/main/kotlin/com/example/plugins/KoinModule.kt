package com.example.plugins

import com.example.security.HashingService
import com.example.security.SHA256HashingService
import com.example.token.JwtTokenService
import com.example.token.TokenConfig
import com.example.token.TokenService
import org.koin.dsl.module
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {

    install(Koin) {
        modules(appModule)
    }
}

val appModule = module {
    single<HashingService> { SHA256HashingService() }
    single<TokenService> { JwtTokenService() }
    single { params ->
        TokenConfig(
            issuer = params.get(),
            audience = params.get(),
            expiresIn = params.get(),
            secret = params.get()
        )
    }
}