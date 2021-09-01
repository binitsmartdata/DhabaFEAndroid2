package com.transport.mall.repository.networkoperator

import okhttp3.ResponseBody

data class ApiResult<out T>(val status: Status, val data: T?, val error: ResponseBody?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): ApiResult<T> {
            return ApiResult(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String?, error: ResponseBody?): ApiResult<T> {
            return ApiResult(Status.ERROR, null, error, message)
        }

        fun <T> loading(data: T? = null): ApiResult<T> {
            return ApiResult(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}