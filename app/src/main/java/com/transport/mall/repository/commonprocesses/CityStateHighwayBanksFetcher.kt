package com.transport.mall.repository.commonprocesses

import android.content.Context
import android.util.Log
import com.transport.mall.R
import com.transport.mall.database.AppDatabase
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.ApiUtils
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CityStateHighwayBanksFetcher {
    companion object {
        var context: Context? = null
        var totalApiCount = 4
        var totalCompletedCount = 0
        var totalSuccessCount = 0

        var failedThings = ""

        public fun getAllData(context: Context, callBack: CallBack) {
            this.context = context

            getCities({
                handleResult(it, context.getString(R.string.cities), callBack)
            })
            getStatesList({
                handleResult(it, context.getString(R.string.states), callBack)
            })
            getAllHighway({
                handleResult(it, context.getString(R.string.highways), callBack)
            })
            getAllBankList({
                handleResult(it, context.getString(R.string.banks), callBack)
            })
        }

        private fun handleResult(it: Boolean, title: String, callBack: CallBack) {
            totalCompletedCount += 1
            if (it) {
                totalSuccessCount += 1
            } else {
                failedThings = if (failedThings.isNotEmpty()) "$failedThings, $title" else title
            }
            if (totalCompletedCount == totalApiCount) {
                if (totalCompletedCount == totalSuccessCount) {
                    callBack.onAllSucceed()
                } else if (totalCompletedCount != totalSuccessCount) {
                    callBack.completedWithSomeErrors(failedThings)
                }
            }
        }

        private fun getCities(callBack: GenericCallBack<Boolean>) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    ApiUtils.getInstance()
                        .executeApi(ApiUtils.getInstance().getApiService()?.getAllCities(SharedPrefsHelper.getInstance(context!!).getUserData().accessToken, "4100", "", "", "1", "ASC", "true"))
                        .collect {
                            when (it.status) {
                                ApiResult.Status.LOADING -> {
                                }
                                ApiResult.Status.ERROR -> {
                                    callBack.onResponse(false)
                                }
                                ApiResult.Status.SUCCESS -> {
                                    it.data?.data?.data?.let {
                                        callBack.onResponse(true)
                                        AppDatabase.getInstance(context!!)?.cityDao()
                                            ?.deleteAll()
                                        for (model in it) {
                                            model.name_en = model.name?.en!!
                                            AppDatabase.getInstance(context!!)?.cityDao()
                                                ?.insert(model)
                                        }
                                    }
                                }
                            }
                        }
                } catch (e: Exception) {
                    callBack.onResponse(false)
                    Log.e("GET CITIES : ", e.toString())
//                    GlobalUtils.showToastInCenter(context, ApiUtils.getInstance().getCorrectErrorMessage(e))
                }
            }
        }

        fun getStatesList(callBack: GenericCallBack<Boolean>) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    ApiUtils.getInstance().executeApi(
                        ApiUtils.getInstance().getApiService()?.getAllStates(
                            SharedPrefsHelper.getInstance(context!!).getUserData().accessToken,
                            "999",
                            "", "", "1", "ASC", "true"
                        )
                    ).collect {
                        when (it.status) {
                            ApiResult.Status.LOADING -> {
                            }
                            ApiResult.Status.ERROR -> {
                                callBack.onResponse(false)
                            }
                            ApiResult.Status.SUCCESS -> {
//                            progressObserverCityStates?.value = false
                                it.data?.data?.data?.let {
                                    callBack.onResponse(true)
                                    AppDatabase.getInstance(context!!)?.statesDao()
                                        ?.deleteAll()
                                    for (model in it) {
                                        model.name_en = model.name?.en!!
                                        AppDatabase.getInstance(context!!)?.statesDao()
                                            ?.insert(model)
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    callBack.onResponse(false)
                    Log.e("GET STATES : ", e.toString())
//                    GlobalUtils.showToastInCenter(context, ApiUtils.getInstance().getCorrectErrorMessage(e))
                }
            }
        }

        fun getAllBankList(callBack: GenericCallBack<Boolean>) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    ApiUtils.getInstance().executeApi(
                        ApiUtils.getInstance().getApiService()?.getAllBankList()
                    ).collect {
                        when (it.status) {
                            ApiResult.Status.LOADING -> {
                            }
                            ApiResult.Status.ERROR -> {
                                callBack.onResponse(false)
                            }
                            ApiResult.Status.SUCCESS -> {
//                            progressObserverCityStates?.value = false
                                it.data?.data?.let {
                                    callBack.onResponse(true)
                                    AppDatabase.getInstance(context!!)?.bankDao()
                                        ?.deleteAll()
                                    AppDatabase.getInstance(context!!)?.bankDao()
                                        ?.insertAll(it)
                                }

                            }
                        }
                    }
                } catch (e: Exception) {
                    callBack.onResponse(false)
                    Log.e("GET BANKS : ", e.toString())
//                    showToastInCenter(context, getCorrectErrorMessage(e))
                }
            }
        }

        fun getAllHighway(callBack: GenericCallBack<Boolean>) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    ApiUtils.getInstance().executeApi(ApiUtils.getInstance().getApiService()?.getAllHighway()).collect {
                        when (it.status) {
                            ApiResult.Status.LOADING -> {
                            }
                            ApiResult.Status.ERROR -> {
                                callBack.onResponse(false)
                            }
                            ApiResult.Status.SUCCESS -> {
                                it.data?.data?.let {
                                    callBack.onResponse(true)
                                    AppDatabase.getInstance(context!!)?.highwayDao()
                                        ?.deleteAll()
                                    AppDatabase.getInstance(context!!)?.highwayDao()
                                        ?.insertAll(it)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    callBack.onResponse(false)
                    Log.e("GET HIGHWAYS : ", e.toString())
//                    showToastInCenter(context, getCorrectErrorMessage(e))
                }
            }
        }
    }

    interface CallBack {
        fun onAllSucceed()
        fun completedWithSomeErrors(failedThings: String)
    }
}