package com.example.util

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val code: Int, val message: String)

sealed class ApiError(message: String) : Throwable(message) {
    class Unauthorized : ApiError("Unauthorized")
    class BadRequest(message: String) : ApiError(message)
    class NotFound : ApiError("Not Found")
    class ParsingError(message: String = "Parsing Error") : ApiError(message)
    class ServerError(message: String = "Server Error") : ApiError(message)
}