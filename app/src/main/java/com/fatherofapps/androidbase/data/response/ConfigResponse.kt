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



    fun checkTypeErr(): String {
        return when (statusCode) {
            401 -> "Unauthorized error"
            403 -> "Forbidden error"
            500 -> "Server error"
            else -> message!!
        }
    }

}