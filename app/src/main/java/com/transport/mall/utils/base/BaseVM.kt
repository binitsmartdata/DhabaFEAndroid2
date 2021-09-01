package com.transport.mall.utils.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import retrofit2.Response
import com.transport.mall.repository.networkoperator.ApiResult

open class BaseVM(context: Application) : AndroidViewModel(context) {

    var showProgressDialog: MutableLiveData<Boolean>? = null

    fun toggleProgressDialog(): MutableLiveData<Boolean>? {
        showProgressDialog = null
        showProgressDialog = MutableLiveData()
        return showProgressDialog
    }

    fun showProgressDialog(context: Application) {
        GlobalUtils.showProgressDialog(context)
    }

    fun hideProgressDialog() {
        GlobalUtils.hideProgressDialog()
    }

    fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
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

    fun getPrefs(context: Application): SharedPrefsHelper {
        return SharedPrefsHelper.getInstance(context)
    }

    fun getAccessToken(context: Application): String {
        return SharedPrefsHelper.getInstance(context).getUserData().accessToken
    }
}