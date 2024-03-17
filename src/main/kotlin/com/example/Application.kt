package com.example

import com.example.config.*
import configureApiExceptions
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main() {
    embeddedServer(Netty, port = 8000, host = "localhost", module = Application::module)
        .start(wait = true)
}

@Suppress("unused")
//  application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    configureSerialization()
    configureHTTP()
    configureKoin()
    configureRouting()
    configureApiExceptions()
    configureDoubleReceive()
    //configureRequestValidation()
}

