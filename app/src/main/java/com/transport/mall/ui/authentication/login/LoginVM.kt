package com.transport.mall.ui.authentication.login

import android.app.Application
import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.R
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.AppDatabase
import com.transport.mall.model.UserModel
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

    val mobilePrefixObservable = ObservableField<String>()
    val mobileObservable = ObservableField<String>()
    var userModel = UserModel()

    var errorResponse: MutableLiveData<String>? = null
    var progressObserver: MutableLiveData<Boolean>? = null
    var progressObserverCityStates: MutableLiveData<Boolean>? = MutableLiveData()

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

    fun doLoginProcess(callBak: GenericCallBackTwoParams<ApiResult.Status, String>) {
        var email = ""
        var password = ""
        emailObservable.get()?.let {
            email = it
        }

        passwordObservable.get()?.let {
            password = it
        }

        when (email.isNotEmpty() && password.isNotEmpty() && isValidEmail(email)) {
            true -> {
                progressObserver?.value = true
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        executeApi(getApiService()?.login(email, password)).collect { result ->
                            when (result.status) {
                                ApiResult.Status.LOADING -> {
                                    callBak.onResponse(result.status, "")
                                }
                                ApiResult.Status.ERROR -> {
                                    progressObserver?.value = false
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
                                    progressObserver?.value = false
                                    result.data?.let {
                                        SharedPrefsHelper.getInstance(app as Context)
                                            .setUserData(result.data.data!!)

                                        getCitiesList(GenericCallBack {
                                            HomeActivity.start(app?.applicationContext!!)
                                            callBak.onResponse(result.status, result.data.message)
                                        })
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        progressObserver?.value = false
                        showToastInCenter(app!!, getCorrectErrorMessage(e))
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
    }

    fun ownerLogin(callBak: GenericCallBackTwoParams<ApiResult.Status, String>) {
        var mobilePrefix = ""
        var mobile = ""
        mobilePrefixObservable.get()?.let {
            mobilePrefix = it
        }

        mobileObservable.get()?.let {
            mobile = it
        }

        when (mobilePrefix.trim().isNotEmpty() && mobile.trim().isNotEmpty() && mobile.trim().length == 10) {
            true -> {
                progressObserver?.value = true
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        executeApi(getApiService()?.ownerLogin("+" + mobilePrefix, "+" + mobilePrefix + mobile)).collect { result ->
                            when (result.status) {
                                ApiResult.Status.LOADING -> {
                                    callBak.onResponse(result.status, "")
                                }
                                ApiResult.Status.ERROR -> {
                                    progressObserver?.value = false
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
                                    progressObserver?.value = false
                                    result.data?.data?.let {

                                        /* SharedPrefsHelper.getInstance(app as Context)
                                             .setUserData(result.data.data!!)*/
                                        userModel = it
                                        callBak.onResponse(result.status, result.data.message)

                                        /*getCitiesList(GenericCallBack {
                                            HomeActivity.start(app?.applicationContext!!)
                                            callBak.onResponse(result.status, result.data.message)
                                        })*/
                                    } ?: run {
                                        showToastInCenter(app!!, result.data?.message.toString())
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        progressObserver?.value = false
                        showToastInCenter(app!!, getCorrectErrorMessage(e))
                    }
                }
            }
            else -> {
                when {
                    mobile.trim().isEmpty() -> errorResponse?.value = app!!.getString(R.string.enter_mobile_number)
                    mobile.trim().length < 10 -> errorResponse?.value = app!!.getString(R.string.enter_valid_mobile)
                }
            }
        }
    }

    fun getCitiesList(callBack: GenericCallBack<Boolean>) {
        progressObserverCityStates?.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(getApiService()?.getAllCities(getAccessToken(app!!), "4100", "", "", "1", "ASC", "true")).collect {
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
            } catch (e: Exception) {
                progressObserverCityStates?.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun getStatesList(callBack: GenericCallBack<Boolean>) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
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
//                            progressObserverCityStates?.value = false
                            it.data?.data?.data?.let {
                                for (model in it) {
                                    model.name_en = model.name?.en!!
                                    AppDatabase.getInstance(app!!)?.statesDao()
                                        ?.insert(model)
                                }
                            }

                            getAllBankList(callBack)
                        }
                    }
                }
            } catch (e: Exception) {
                progressObserverCityStates?.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun getAllBankList(callBack: GenericCallBack<Boolean>) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.getAllBankList()
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
//                            progressObserverCityStates?.value = false
                            it.data?.data?.let {
                                AppDatabase.getInstance(app!!)?.bankDao()
                                    ?.insertAll(it)
                            }

                            getAllHighway(callBack)
                        }
                    }
                }
            } catch (e: Exception) {
                progressObserverCityStates?.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun getAllHighway(callBack: GenericCallBack<Boolean>) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(getApiService()?.getAllHighway()).collect {
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
                            it.data?.data?.let {
                                AppDatabase.getInstance(app!!)?.highwayDao()
                                    ?.insertAll(it)
                            }
                            callBack.onResponse(true)
                        }
                    }
                }
            } catch (e: Exception) {
                progressObserverCityStates?.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

}