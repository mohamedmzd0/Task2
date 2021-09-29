package com.appsquare.task.data

sealed class ApiResources<out T>(
    val status: ApiStatus,
    val responseCode: Int?,
    val data: T?,
    val message: String?
) {

    data class Success<out R>(val _data: R?, val _responseCode: Int) : ApiResources<R>(
        status = ApiStatus.SUCCESS,
        responseCode = _responseCode,
        data = _data,
        message = null
    )

    data class Error(val exception: String, val _responseCode: Int?=null) : ApiResources<Nothing>(
        status = ApiStatus.ERROR,
        responseCode = _responseCode,
        data = null,
        message = exception
    )

    data class Loading<out R>(val isLoading: Boolean) : ApiResources<R>(
        status = ApiStatus.LOADING,
        responseCode = null,
        data = null,
        message = null
    )
}

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}