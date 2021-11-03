package com.transport.mall.ui.authentication.otpVerification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.AppDatabase
import com.transport.mall.model.UserModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class OtpVerificationVM(application: Application) : BaseVM(application) {
    var progressObserver: MutableLiveData<Boolean>? = null
    var resendOtpObserver: MutableLiveData<Boolean>? = MutableLiveData<Boolean>()
    var progressObserverCityStates: MutableLiveData<Boolean>? = MutableLiveData<Boolean>()
    var app: Application? = null

    var otp: String = ""
    var userModel = UserModel()

    init {
        app = application
    }

    fun observerProgress(): MutableLiveData<Boolean>? {
        progressObserver = null
        progressObserver = MutableLiveData()
        return progressObserver
    }

    fun checkOtp(callBack: GenericCallBack<ApiResponseModel<UserModel>>) {
        progressObserver?.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.checkOtp(userModel._id, otp)
                ).collect {
                    handleResponse(it, callBack)
                }
            } catch (e: Exception) {
                progressObserver?.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun resendOtp(callBack: GenericCallBack<ApiResponseModel<UserModel>>) {
        resendOtpObserver?.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.resendOtp("+" + userModel.mobilePrefix, userModel.mobile)
                ).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            resendOtpObserver?.value = true
                        }
                        ApiResult.Status.ERROR -> {
                            resendOtpObserver?.value = false
                            try {
                                callBack.onResponse(Gson().fromJson(it.error?.string(), ApiResponseModel::class.java) as ApiResponseModel<UserModel>?)
                            } catch (e: Exception) {
                                callBack.onResponse(ApiResponseModel(0, it.message!!, null))
                            }
                        }
                        ApiResult.Status.SUCCESS -> {
                            resendOtpObserver?.value = false
                            callBack.onResponse(it.data)
                        }
                    }
                }
            } catch (e: Exception) {
                resendOtpObserver?.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    private fun handleResponse(
        it: ApiResult<ApiResponseModel<UserModel>>,
        callBack: GenericCallBack<ApiResponseModel<UserModel>>
    ) {
        when (it.status) {
            ApiResult.Status.LOADING -> {
                progressObserver?.value = true
            }
            ApiResult.Status.ERROR -> {
                progressObserver?.value = false
                try {
                    callBack.onResponse(Gson().fromJson(it.error?.string(), ApiResponseModel::class.java) as ApiResponseModel<UserModel>?)
                } catch (e: Exception) {
                    callBack.onResponse(ApiResponseModel(0, it.message!!, null))
                }
            }
            ApiResult.Status.SUCCESS -> {
                progressObserver?.value = false
                if (it.data?.status == 200) {
                    getCitiesList(GenericCallBack { isSuccess ->
                        callBack.onResponse(it.data)
                    })
                } else {
                    callBack.onResponse(it.data)
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
                                AppDatabase.getInstance(app!!)?.cityDao()
                                    ?.deleteAll()
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
                                AppDatabase.getInstance(app!!)?.statesDao()
                                    ?.deleteAll()
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
                                    ?.deleteAll()
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
                                    ?.deleteAll()
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
