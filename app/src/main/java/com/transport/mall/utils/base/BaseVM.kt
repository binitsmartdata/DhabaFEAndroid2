package com.transport.mall.utils.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.healthiex.naha.repository.networkoperator.Result
import retrofit2.Response

open class BaseVM(context: Application) : AndroidViewModel(context) {

    var showProgressDialog: MutableLiveData<Boolean>? = null

    fun toggleProgressDialog(): MutableLiveData<Boolean>? {
        showProgressDialog = null
        showProgressDialog = MutableLiveData()
        return showProgressDialog
    }

    fun showProgressDialog() {
        showProgressDialog?.value = true
    }

    fun hideProgressDialog() {
        showProgressDialog?.value = false
    }

    suspend fun <T> getResponse(request: suspend () -> Response<T>?): Result<T> {
        return try {
            val result = request.invoke()
            if (result?.isSuccessful!!) {
                return Result.success(result.body())
            } else {
                Result.error(result?.message(), result?.errorBody())
            }
        } catch (e: Throwable) {
            Result.error("SERVER_ERROR", null)
        }
    }
}