package com.example.util

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val code: Int, val message: String)

sealed class ApiError(val statusCode: Int, override val message: String, val code: Int? = null):Throwable() {
    class BadRequest(message: String = "Bad Request", code: Int? = null) : ApiError(400, message, code)
    class Unauthorized(message: String = "Unauthorized", code: Int? = null) : ApiError(401, message, code)
    class Forbidden(message: String = "Forbidden", code: Int? = null) : ApiError(403, message, code)
    class NotFound(message: String = "Not Found", code: Int? = null) : ApiError(404, message, code)
    class MethodNotAllowed(message: String = "Method Not Allowed", code: Int? = null) : ApiError(405, message, code)
    class Conflict(message: String = "Conflict", code: Int? = null) : ApiError(409, message, code)
    class UnprocessableEntity(message: String = "Unprocessable Entity", code: Int? = null) : ApiError(422, message, code)
    class TooManyRequests(message: String = "Too Many Requests", code: Int? = null) : ApiError(429, message, code)
    class InternalServerError(message: String = "Internal Server Error", code: Int? = null) : ApiError(500, message, code)
}