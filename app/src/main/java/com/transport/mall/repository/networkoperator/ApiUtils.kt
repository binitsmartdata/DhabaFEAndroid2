package com.transport.mall.repository.networkoperator

import android.net.ParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.FileNotFoundException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class ApiUtils {
    companion object {
        var apiUtils: ApiUtils? = null
        public fun getInstance(): ApiUtils {
            apiUtils?.let {
                return it
            } ?: kotlin.run {
                apiUtils = ApiUtils()
                return apiUtils!!
            }
        }
    }

    suspend fun <T> executeApi(input: Response<T>?): Flow<ApiResult<T>> {
        return flow {
            try {
                emit(ApiResult.loading<T>())
                emit(getResponse(request = { input }))
            } catch (e: Exception) {
                emit(ApiResult.error<T>(e.toString(), null))
            }
        }
    }

    suspend fun <T> getResponse(request: suspend () -> Response<T>?): ApiResult<T> {
        return try {
            val result = request.invoke()
            if (result?.isSuccessful!!) {
                return ApiResult.success(result.body())
            } else {
                return ApiResult.error(result.message(), result.errorBody())
            }
        } catch (e: Throwable) {
            ApiResult.error(getCorrectErrorMessage(e), null)
        }
    }

    fun getCorrectErrorMessage(t: Throwable): String {
        var error_code: Int = 0
        try {
            if (t is SocketTimeoutException) {
                error_code = ResponseCodes.TIMEOUT_EXCEPTION
            } else if (t is FileNotFoundException) {
                error_code = ResponseCodes.TIMEOUT_EXCEPTION
            } else if (t is JsonSyntaxException) {
                error_code = ResponseCodes.JSON_SYNTAX_EXCEPTION
            } else if (t is TimeoutException) {
                error_code = ResponseCodes.TIMEOUT_EXCEPTION
            } else if (t is ClassCastException) {
                error_code = ResponseCodes.MODEL_TYPE_CAST_EXCEPTION
            } else if (t is MalformedJsonException) {
                error_code = ResponseCodes.MODEL_TYPE_CAST_EXCEPTION
            } else if (t is ParseException) {
                error_code = ResponseCodes.MODEL_TYPE_CAST_EXCEPTION
            } else if (t is UnknownHostException) {
                error_code = ResponseCodes.URL_CONNECTION_ERROR
            } else if (t is ConnectException) {
                error_code = ResponseCodes.INTERNET_NOT_AVAILABLE
            } else {
                val errorMessage = (t as HttpException).response()?.errorBody()!!.string()
                val responseCode = t.response()?.code()
                responseCode?.let {
                    error_code = it
                }
                return errorMessage
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            error_code = ResponseCodes.UNKNOWN_ERROR
        } finally {
            if (error_code == 0) {
                return t.message.toString()
            }
        }
        return ResponseCodes.logErrorMessage(error_code)
    }

    fun getApiService(): ApiService? {
        return NetworkAdapter.getInstance().getNetworkServices()
    }
}