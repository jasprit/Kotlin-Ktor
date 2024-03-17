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
        exception<Throwable> { call, throwable ->
            val statusCode = when (throwable) {
                is ApiError.Unauthorized -> HttpStatusCode.Unauthorized
                is SerializationException, is ApiError.ParsingError, is RequestValidationException ->
                    HttpStatusCode.UnprocessableEntity

                is ApiError.ServerError -> HttpStatusCode.InternalServerError
                else -> HttpStatusCode.BadRequest
            }
            call.respond(statusCode, ErrorResponse(statusCode.value, throwable.message.toString()))
        }
    }
}