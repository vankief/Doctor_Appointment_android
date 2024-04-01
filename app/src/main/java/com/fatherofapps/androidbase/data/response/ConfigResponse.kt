package com.fatherofapps.androidbase.data.response

data class ErrMessage(
    val message: String,
    val statusCode: Int
) {
    fun checkType(): String {
        return when (statusCode) {
            401 -> "unauthorized"
            403 -> "forbidden"
            500 -> "server"
            else -> "unknown"
        }
    }
}
data class ConfigResponse<T>(
    val status: String?,
    val message: String?,
    val statusCode: Int?,
    val reasonStatusCode: String?,
    val data: T?
) {
    fun isSuccess(): Boolean {
        return status == "success"
    }

    fun isError(): Boolean {
        return status == "error"
    }

    fun isUnauthorized(): Boolean {
        return statusCode == 401
    }

    fun isForbidden(): Boolean {
        return statusCode == 403
    }

    fun isErrorServer(): Boolean {
        return statusCode == 500
    }

    fun getMess(): String {
        return message ?: "unknown"
    }


    fun checkTypeErr(): String {
        return when (statusCode) {
            401 -> "Unauthorized error"
            403 -> "Forbidden error"
            500 -> "Server error"
            else -> message!!
        }
    }

}

sealed class _ConfigData<out T, out X>
data class TData<T>(val data: T) : _ConfigData<T, Nothing>()
data class XData<X>(val data: X) : _ConfigData<Nothing, X>()
data class _ConfigResponse<T, X>(
    val status: String?,
    val message: String?,
    val statusCode: Int?,
    val reasonStatusCode: String?,
    val data: _ConfigData<T, X>?
) {
    fun isSuccess(): Boolean {
        return status == "success"
    }

    fun isError(): Boolean {
        return status == "error"
    }

    fun isUnauthorized(): Boolean {
        return statusCode == 401
    }

    fun isForbidden(): Boolean {
        return statusCode == 403
    }

    fun isErrorServer(): Boolean {
        return statusCode == 500
    }

    fun getResponse (): _ConfigData<T, X> {
        return if(message === "ONLINE") {
            data as TData
        } else {
            data as XData
        }
    }



    fun checkTypeErr(): String {
        return when (statusCode) {
            401 -> "Unauthorized error"
            403 -> "Forbidden error"
            500 -> "Server error"
            else -> message!!
        }
    }

}