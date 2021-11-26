package com.transport.mall.ui.authentication.otpVerification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.R
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.AppDatabase
import com.transport.mall.model.UserModel
import com.transport.mall.repository.commonprocesses.CityStateHighwayBanksFetcher
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
                if (it.data?.status == 200) {
//                    getCitiesList(GenericCallBack { isSuccess ->
//                        callBack.onResponse(it.data)
//                    })

                    // FETCHING CITIES, STATES, HIGHWAYS, BANKS
                    CityStateHighwayBanksFetcher.getAllData(app!!, object : CityStateHighwayBanksFetcher.CallBack {
                        override fun onAllSucceed() {
                            callBack.onResponse(it.data)
                            progressObserver?.value = false
                        }

                        override fun completedWithSomeErrors(failedThings: String) {
                            progressObserver?.value = false
                            showToastInCenter(app!!, app!!.getString(R.string.failed_to_fetch) + " " + failedThings)
                        }
                    })
                    //------------

                } else {
                    callBack.onResponse(it.data)
                }
            }
        }
    }
}
