import com.example.util.ApiError
import com.example.util.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException


fun Application.configureApiExceptions() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val apiError = when (cause) {
                is ApiError -> cause // If the throwable is an instance of ApiError, use it directly
                is SerializationException -> ApiError.BadRequest("Failed to deserialize request body", 4001)
                is RequestValidationException -> ApiError.UnprocessableEntity("Request validation failed", 4002)
                else -> ApiError.InternalServerError("An unexpected error occurred", 5001)
            }
            call.respond(HttpStatusCode.fromValue(apiError.statusCode), ErrorResponse(apiError.code ?: apiError.statusCode, apiError.message))
        }
    }
}