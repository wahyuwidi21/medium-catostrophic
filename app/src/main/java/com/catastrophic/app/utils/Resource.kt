package com.catastrophic.app.utils


data class Resource<T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    enum class Status {
        SUCCESS,
        LOADING,
        ERROR
    }

    companion object {
        fun <T> success(data: T?): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = Status.LOADING, data = data, message = null)

        fun <T> error(message: String?, data: T? = null, throwable: Throwable): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)
    }
}
