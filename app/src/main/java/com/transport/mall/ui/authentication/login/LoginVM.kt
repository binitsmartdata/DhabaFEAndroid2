package com.transport.mall.ui.authentication.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.R
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.AppDatabase
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.home.HomeActivity
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Parambir Singh on 2021-08-25.
 */
class LoginVM(application: Application) : BaseVM(application) {
    val emailObservable = ObservableField<String>()
    val passwordObservable = ObservableField<String>()

    var errorResponse: MutableLiveData<String>? = null
    var progressObserver: MutableLiveData<Boolean>? = null
    var progressObserverCityStates: MutableLiveData<Boolean>? = null

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
                progressObserver?.value = true
                GlobalScope.launch(Dispatchers.Main) {
                    executeApi(getApiService()?.login(email, password)).collect { result ->
                        when (result.status) {
                            ApiResult.Status.LOADING -> {
                                callBak.onResponse(result.status, "")
                            }
                            ApiResult.Status.ERROR -> {
                                try {
                                    val response =
                                        Gson().fromJson(
                                            result.error?.string(),
                                            ApiResponseModel::class.java
                                        )
                                    callBak.onResponse(result.status, response.message)
                                } catch (e: Exception) {
                                    callBak.onResponse(result.status, result.message)
                                }
                            }
                            ApiResult.Status.SUCCESS -> {
                                SharedPrefsHelper.getInstance(app as Context)
                                    .setUserData(result.data?.data!!)

                                getCitiesList(GenericCallBack {
                                    HomeActivity.start(app?.applicationContext!!)
                                    callBak.onResponse(result.status, result.data.message)
                                })
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

    fun getCitiesList(callBack: GenericCallBack<Boolean>) {
        progressObserverCityStates?.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.getAllCities(
                    getAccessToken(app!!),
                    "4100",
                    "", "", "1", "ASC", "true"
                )
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserverCityStates?.value = true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserverCityStates?.value = false
                    }
                    ApiResult.Status.SUCCESS -> {
                        it.data?.data?.data?.let {
                            for (model in it) {
                                model.name_en = model.name?.en!!
                                AppDatabase.getInstance(app!!)?.cityDao()
                                    ?.insert(model)
                            }
                        }
                        getStatesList(callBack)
                    }
                }
            }
        }
    }

    fun getStatesList(callBack: GenericCallBack<Boolean>) {
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.getAllStates(
                    getAccessToken(app!!),
                    "999",
                    "", "", "1", "ASC", "true"
                )
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserverCityStates?.value = true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserverCityStates?.value = false
                        callBack.onResponse(false)
                    }
                    ApiResult.Status.SUCCESS -> {
                        progressObserverCityStates?.value = false
                        it.data?.data?.data?.let {
                            for (model in it) {
                                model.name_en = model.name?.en!!
                                AppDatabase.getInstance(app!!)?.statesDao()
                                    ?.insert(model)
                            }
                        }

                        getAllHighway(callBack)
                    }
                }
            }
        }
    }

    fun getAllHighway(callBack: GenericCallBack<Boolean>) {
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.getAllHighway()
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserverCityStates?.value = true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserverCityStates?.value = false
                    }
                    ApiResult.Status.SUCCESS -> {
                        progressObserverCityStates?.value = false
                        it.data?.data?.let {
                            AppDatabase.getInstance(app!!)?.highwayDao()
                                ?.insertAll(it)
                        }
                        callBack.onResponse(true)
                    }
                }
            }
        }
    }

}