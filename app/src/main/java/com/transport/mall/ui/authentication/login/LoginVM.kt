package com.transport.mall.ui.authentication.login

import android.app.Application
import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.R
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.UserModel
import com.transport.mall.repository.commonprocesses.CityStateHighwayBanksFetcher
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.home.HomeActivity
import com.transport.mall.utils.base.BaseVM
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
                                    result.data?.let {
                                        // FETCHING CITIES, STATES, HIGHWAYS, BANKS
                                        CityStateHighwayBanksFetcher.getAllData(app!!, object : CityStateHighwayBanksFetcher.CallBack {
                                            override fun onAllSucceed() {
                                                SharedPrefsHelper.getInstance(app as Context)
                                                    .setUserData(result.data.data!!)
                                                callBak.onResponse(result.status, result.data.message)
                                                progressObserver?.value = false
                                            }

                                            override fun completedWithSomeErrors(failedThings: String) {
                                                progressObserver?.value = false
                                                showToastInCenter(app!!, app!!.getString(R.string.failed_to_fetch) + " " + failedThings)
                                            }
                                        })
                                        //---------------
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
                        executeApi(getApiService()?.ownerLogin("+" + mobilePrefix, mobile)).collect { result ->
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
}