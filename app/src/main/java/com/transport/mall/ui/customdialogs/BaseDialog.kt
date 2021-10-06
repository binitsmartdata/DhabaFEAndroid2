package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.ApiService
import com.transport.mall.repository.networkoperator.NetworkAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


open class BaseDialog(context: Context) : Dialog(context) {

    val limit = "10"

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
            ApiResult.error(e.message, null)
        }
    }

    fun getApiService(): ApiService? {
        return NetworkAdapter.getInstance().getNetworkServices()
    }
}