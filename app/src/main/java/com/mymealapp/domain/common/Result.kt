package com.mymealapp.domain.common

/**
 * This class differs from RESOURCE, because it was used with pagination.
 */

/**
 * Class to wrap responses handling 3 possible states: Success, Error, Exception
 */
sealed class Result<T> {
    class Success<T>(val data: T) : Result<T>()
    class Error<T : Any>(val code: Int, val message: String?) : Result<T>()
    class Exception<T : Any>(val exception: kotlin.Exception) : Result<T>()
}

/**
 * Extension function to handle in an easier way the possible states with lambdas
 */
inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onError: (code: Int, message: String?) -> R,
    onException: (exception: Exception) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(code, message)
    is Result.Exception -> onException(exception)
}

/**
 * Extension function to retrieve only the success value
 */
fun <T> Result<T>.getSuccess() = when (this) {
    is Result.Success -> data
    else -> null
}