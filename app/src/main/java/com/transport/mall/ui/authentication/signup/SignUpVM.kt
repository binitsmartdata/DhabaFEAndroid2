package com.transport.mall.ui.authentication.signup

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.R
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
 * Created by Parambir Singh on 2021-08-25.
 */
class SignUpVM(application: Application) : BaseVM(application) {
    val nameObervable = ObservableField<String>()
    val phoneNumberObserver = ObservableField<String>()
    val emailObservable = ObservableField<String>()
    val mobilePrefixObserver = ObservableField<String>()

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

    fun signUp(callBack: GenericCallBack<ApiResponseModel<UserModel>>) {
        var name = ""
        var phone = ""
        var prefix = ""
        var email = ""
        nameObervable.get()?.let {
            name = it
        }
        phoneNumberObserver.get()?.let {
            phone = it
        }
        mobilePrefixObserver.get()?.let {
            prefix = it
        }
        emailObservable.get()?.let {
            email = it
        }

        when (name.isNotEmpty() && phone.isNotEmpty()/* && (email.isNotEmpty() && isValidEmail(email))*/) {
            true -> {
                progressObserver?.value = true
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        executeApi(
                            getApiService()?.signup(
                                name,
                                phone,
                                prefix
//                                email
                            )
                        ).collect { result ->
                            when (result.status) {
                                ApiResult.Status.LOADING -> {
                                    progressObserver?.value = true
                                }
                                ApiResult.Status.ERROR -> {
                                    progressObserver?.value = false
                                    try {
                                        callBack.onResponse(Gson().fromJson(result.error?.string(), ApiResponseModel::class.java) as ApiResponseModel<UserModel>)
                                    } catch (e: Exception) {
                                        callBack.onResponse(ApiResponseModel(0, result.message.toString(), null))
                                    }
                                }
                                ApiResult.Status.SUCCESS -> {
                                    progressObserver?.value = false
                                    callBack.onResponse(result.data)
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
                    name.trim().isEmpty() ->
                        errorResponse?.value = app?.getString(R.string.please_enter_name)
                    phone.trim().isEmpty() || phone.trim().length < 10 -> errorResponse?.value =
                        app?.getString(R.string.enter_valid_phone_number)
                    /*email.trim().isEmpty() -> errorResponse?.value =
                        app?.getString(R.string.email_empty_validation)
                    !isValidEmail(email) -> errorResponse?.value =
                        app?.getString(R.string.valid_email_validation)*/
                }
            }
        }
    }

    fun getCitiesList(callBack: GenericCallBack<Boolean>) {
        progressObserverCityStates?.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.getAllCities(
                        getAccessToken(app!!),
                        "4100",
                        "",
                        "",
                        "1",
                        "ASC",
                        "true"
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