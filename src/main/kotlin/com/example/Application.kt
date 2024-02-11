package com.example

import com.example.feature.user.UserServicesImpl
import com.example.plugins.*
import com.example.security.SHA256HashingService
import com.example.token.JwtTokenService
import com.example.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


fun main() {
    embeddedServer(Netty, port = 8000, host = "localhost", module = Application::module)
        .start(wait = true)
}

@Suppress("unused")
//  application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val mongoUsrName = System.getenv("MONGO_USR_NAME")
    val mongoPw = System.getenv("MONGO_PWD")
    val mongoDbName = System.getenv("MONGO_DB_NAME")
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://$mongoUsrName:$mongoPw@cluster0.n3lt88l.mongodb.net/$mongoDbName?retryWrites=true&w=majority"

    ).coroutine
        .getDatabase(mongoDbName)

    val userServices = UserServicesImpl(db)

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = System.getenv("jwt.issuer"),
        audience = System.getenv("jwt.audience"),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSerialization()
    configureHTTP()
    configureRouting(userServices, hashingService,tokenService,tokenConfig)
    configureDoubleReceive()
    configureRequestValidation()
}

