package com.transport.mall.utils.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.healthiex.naha.repository.networkoperator.Result
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import retrofit2.Response

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

    suspend fun <T> getResponse(request: suspend () -> Response<T>?): Result<T> {
        return try {
            val result = request.invoke()
            if (result?.isSuccessful!!) {
                return Result.success(result.body())
            } else {
                return Result.error(result.message(), result.errorBody())
            }
        } catch (e: Throwable) {
            Result.error(e.message, null)
        }
    }

    fun getPrefs(context: Application): SharedPrefsHelper {
        return SharedPrefsHelper.getInstance(context)
    }

    fun getAccessToken(context: Application): String {
        return SharedPrefsHelper.getInstance(context).getUserData().accessToken
    }
}