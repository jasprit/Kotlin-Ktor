package com.example.util

import io.ktor.server.plugins.requestvalidation.ValidationResult
import java.time.Duration
import java.time.temporal.Temporal
import kotlin.reflect.KProperty


class ValidationBuilder {

    val violations = mutableListOf<String>()

    inline fun violation(block: () -> String) = violations.add(block())

    fun <T : Any> KProperty<T>.validate(
        message: String? = null,
        predicate: T.() -> Boolean,
    ): KProperty<T> = apply {
        val value = runCatching { getter.call() }.getOrElse {
            throw RuntimeException("Could not get value of $name. Please use this::$name")
        }
        if (!value.predicate()) {
            violation { message ?: "$jsonName is invalid" }
        }
    }

    fun KProperty<String>.notBlank(message: String? = null): KProperty<String> =
        validate((message ?: "$jsonName cannot be blank")) { isNotBlank() }

    fun KProperty<String>.length(range: LongRange, message: String? = null): KProperty<String> =
        validate(
            message ?: "$jsonName must be between ${range.first} and ${range.last} characters long"
        ) {
            length in range
        }

    fun KProperty<String>.isValidPassword(
        password: String,
        message: String? = null
    ): KProperty<String> =
        validate(
            message ?: "$jsonName password condition not met"
        ) {
            length(8L..20L)
            password.matches(regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$"))
        }

    fun KProperty<String>.isValidEmail(email: String, message: String? = null): KProperty<String> =
        validate(
            message ?: "$jsonName is invalid $email"
        ) {
            length(2L..30L)
            email.matches(regex = Regex("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$"))
        }

    fun KProperty<String>.length(max: Long, message: String? = null): KProperty<String> =
        validate(message ?: "$jsonName must be between 0 and $max characters long") {
            length in 0..max
        }

//	fun KProperty<String>.validUrl(message: String? = null): KProperty<String> =
//		validate(message ?: "$jsonName must be a valid URL: https://example.com") { isValidUrl() }

//	fun KProperty<String>.validUUID(message: String? = null): KProperty<String> =
//		validate(message ?: "$jsonName must be a valid UUID: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx") {
//			toUUID().isSuccess
//		}

    fun <T : Comparable<T>> KProperty<T>.range(
        range: ClosedRange<T>,
        message: String? = null
    ): KProperty<T> =
        validate(message ?: "$jsonName must be between ${range.start} and ${range.endInclusive}") {
            this in range
        }

    fun <T : Comparable<T>> KProperty<T>.max(max: T, message: String? = null): KProperty<T> =
        validate(message ?: "$jsonName must be less than $max") { this <= max }

    fun <T : Comparable<T>> KProperty<T>.min(min: T, message: String? = null): KProperty<T> =
        validate(message ?: "$jsonName must be greater than $min") { this >= min }

    fun <T : Temporal> KProperty<T>.before(before: T, message: String? = null): KProperty<T> =
        validate(message ?: "$jsonName must be before $before") {
            Duration.between(before, this).let { it.isPositive || it.isZero }
        }

    fun <T : Temporal> KProperty<T>.after(after: T, message: String? = null): KProperty<T> =
        validate(message ?: "$jsonName must be after $after") {
            Duration.between(after, this).let { it.isNegative || it.isZero }
        }

    fun <T : Temporal> KProperty<T>.between(from: T, to: T, message: String? = null): KProperty<T> =
        validate(message ?: "$jsonName must be between from $from and to $to") {
            Duration.between(from, this).let { it.isPositive || it.isZero }
                    && Duration.between(to, this).let { it.isNegative || it.isZero }
        }
}

private val Duration.isPositive
    get() = !isNegative

private val KProperty<*>.jsonName
    get() = /*getter.findAnnotation<JsonProperty>()?.value*/ // if you use jackson
    /*?: getter.findAnnotation<SerialName>()?.value*/ // if you use kotlinx serialization
        /*?:*/ name // if you use none of above

inline fun validateAll(block: ValidationBuilder.() -> Unit): ValidationResult {
    val violations = ValidationBuilder().apply(block).violations
    return if (violations.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(violations)
}


