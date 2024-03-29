package com.mymealapp.domain.common

import androidx.annotation.StringRes
import com.mymealapp.R
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.UnknownHostException

/**
 * Class to wrap possible errors and handle them properly
 */
sealed class Error {
    object Connectivity : Error()
    object InternetConnection : Error()
    data class NotFoundMovie(@StringRes val messageResId: Int) : Error()
    data class Unknown(val message: String) : Error()
    class HttpException(@StringRes val messageResId: Int) : Error()
}

/**
 * Convert a exception into a Error validating which exception happened
 * @return Error
 */
fun Exception.toError(): Error = when (this) {
    is UnknownHostException -> Error.InternetConnection
    is IOException -> Error.Connectivity
    is HttpException -> HttpErrors.handleError(code())
    else -> Error.Unknown(message ?: "")
}

/**
 * Convert a throwable into a Error validating which exception happened
 * @return Error
 */
fun Throwable.toError(): Error = when (this) {
    is UnknownHostException -> Error.InternetConnection
    is IOException -> Error.Connectivity
    is HttpException -> HttpErrors.handleError(code())
    else -> Error.Unknown(message ?: "")
}

/**
 * With a specific code int value, uses handle error function to validate and return a http exception Error
 * @return Error
 */
fun Int.validateHttpErrorCode(): Error {
    return HttpErrors.handleError(this)
}

object HttpErrors {
    /**
     * Given a code error, validate and returns a http exception error
     * @param errorCode: value to validate and handle error code
     * @return Error
     */
    fun handleError(errorCode: Int): Error {
        if (errorCode == HttpURLConnection.HTTP_NOT_FOUND) {
            return Error.NotFoundMovie(R.string.not_found_error)
        }
        val errorResId = when (errorCode) {
            503 -> R.string.service_unavailable_error
            500 -> R.string.internal_server_error
            400 -> R.string.invalid_request_error
            401 -> R.string.unauthorized_error
            else -> R.string.unknown_error
        }
        return Error.HttpException(messageResId = errorResId)
    }
}