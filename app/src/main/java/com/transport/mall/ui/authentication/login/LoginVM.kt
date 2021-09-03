package com.transport.mall.ui.authentication.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.R
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.UserModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.NetworkAdapter
import com.transport.mall.ui.home.HomeActivity
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * Created by Parambir Singh on 2021-08-25.
 */
class LoginVM(application: Application) : BaseVM(application) {
    val emailObservable = ObservableField<String>()
    val passwordObservable = ObservableField<String>()

    var errorResponse: MutableLiveData<String>? = null
    var progressObserver: MutableLiveData<Boolean>? = null


    var app: Application? = null

    init {
        app = application
    }

    fun observerError(): MutableLiveData<String>? {
        errorResponse = null
        errorResponse = MutableLiveData()
        return errorResponse
    }

    fun observerProgress(): MutableLiveData<Boolean>? {
        progressObserver = null
        progressObserver = MutableLiveData()
        return progressObserver
    }

    fun doLoginProcess(callBak: GenericCallBackTwoParams<com.transport.mall.repository.networkoperator.ApiResult.Status, String>) {
        Log.e("doLoginProcess", "-------------------")
        var email = ""
        var password = ""
        emailObservable.get()?.let {
            email = it
        }

        passwordObservable.get()?.let {
            password = it
        }

        when (email.isNotEmpty() && password.isNotEmpty()) {
            true -> {
                GlobalScope.launch(Dispatchers.Main) {
                    login(email, password).collect {
                        when (it.status) {
                            ApiResult.Status.LOADING -> {
                                callBak.onResponse(it.status, "")
                            }
                            ApiResult.Status.ERROR -> {
                                try {
                                    val response =
                                        Gson().fromJson(
                                            it.error?.string(),
                                            ApiResponseModel::class.java
                                        )
                                    callBak.onResponse(it.status, response.message)
                                } catch (e: Exception) {
                                    callBak.onResponse(it.status, it.message)
                                }
                            }
                            ApiResult.Status.SUCCESS -> {
                                SharedPrefsHelper.getInstance(app as Context)
                                    .setUserData(it.data?.data!!)

                                HomeActivity.start(app?.applicationContext!!)
                                callBak.onResponse(it.status, it.data.message)
                            }
                        }
                    }
                }
            }
            else -> {
                when {
                    email.isEmpty() -> errorResponse?.value =
                        app?.getString(R.string.email_empty_validation)
                    !isValidEmail(email) -> errorResponse?.value =
                        app?.getString(R.string.valid_email_validation)
                    password.isEmpty() -> errorResponse?.value =
                        app?.getString(R.string.password_validation)
                }
            }
        }
        progressObserver?.value = true
    }

    suspend fun login(
        email: String,
        password: String
    ): Flow<ApiResult<ApiResponseModel<UserModel>>> {
        return flow {
            emit(ApiResult.loading())
            emit(
                getResponse(
                    request = {
                        NetworkAdapter.getInstance().getNetworkServices()?.login(
                            email,
                            password
                        )
                    }
                )
            )
        }.flowOn(Dispatchers.IO)
    }
}