package com.transport.mall.ui.home.dhabalist

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.FiltersModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class DhabaListVM(application: Application) : BaseVM(application) {
    var errorResponse: MutableLiveData<String>? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var dialogProgressObserver: MutableLiveData<Boolean> = MutableLiveData()

    var app: Application? = null

    init {
        app = application
    }

    fun observerError(): MutableLiveData<String>? {
        errorResponse = null
        errorResponse = MutableLiveData()
        return errorResponse
    }

    fun getAllDhabaList(
        token: String,
        limit: String,
        page: String,
        filters: FiltersModel,
        search: String,
        status: String?,
        callBack: GenericCallBack<ArrayList<DhabaModelMain>>
    ) {
        progressObserver.value = page == "1"
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    if (status == null || status.isEmpty()) {
                        getApiService()?.getAllOwnerDhabaList(
                            token,
                            limit,
                            page,
                            null,
                            GlobalUtils.getNullifEmpty(filters.cities),
                            GlobalUtils.getNullifEmpty(filters.states),
                            GlobalUtils.getNullifEmpty(filters.highway),
                            GlobalUtils.getNullifEmpty(filters.pincode),
                            GlobalUtils.getNullifEmpty(search),
//                            SharedPrefsHelper.getInstance(app?.applicationContext!!).getUserData().role
                            null
                        )
                    } else {
                        getApiService()?.getAllDhabaList(
                            token,
                            limit,
                            page,
                            status,
                            GlobalUtils.getNullifEmpty(filters.cities),
                            GlobalUtils.getNullifEmpty(filters.states),
                            GlobalUtils.getNullifEmpty(filters.highway),
                            GlobalUtils.getNullifEmpty(filters.pincode),
                            GlobalUtils.getNullifEmpty(search),
                            SharedPrefsHelper.getInstance(app?.applicationContext!!).getUserData().role
                        )
                    }
                ).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            progressObserver.value = page == "1"
                        }
                        ApiResult.Status.ERROR -> {
                            progressObserver.value = false
                            callBack.onResponse(ArrayList())
                            if (page == "1") {
                                try {//SHOW ERROR MESSAGE IF OCCURS ON FIRST PAGE
                                    showToastInCenter(app!!, Gson().fromJson(it.error?.string(), ApiResponseModel::class.java).message)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        ApiResult.Status.SUCCESS -> {
                            progressObserver.value = false
                            callBack.onResponse(it.data?.data?.data)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("GET ALL DHABA :", e.toString())
                progressObserver.value = false
//                showToastInCenter(app!!, getCorrectErrorMessage(e))
                callBack.onResponse(ArrayList())
            }
        }
    }
}