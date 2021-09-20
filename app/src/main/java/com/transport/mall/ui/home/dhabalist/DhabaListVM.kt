package com.transport.mall.ui.home.dhabalist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.AppDatabase
import com.transport.mall.model.CityModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
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
        limit: String,
        page: String,
        callBack: GenericCallBack<ArrayList<DhabaModelMain>>
    ) {
        progressObserver.value = page == "1"
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.getAllDhabaList(limit, page)
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value = page == "1"
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                        callBack.onResponse(ArrayList())
                    }
                    ApiResult.Status.SUCCESS -> {
                        progressObserver.value = false
                        callBack.onResponse(it.data?.data?.data)
                    }
                }
            }
        }
    }

    fun getDhabaById(dhabaId: String, callBack: GenericCallBack<ApiResponseModel<DhabaModelMain>>) {
        dialogProgressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.getDhabaByID(dhabaId)
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        dialogProgressObserver.value =
                            true
                    }
                    ApiResult.Status.ERROR -> {
                        dialogProgressObserver.value = false
                        callBack.onResponse(
                            ApiResponseModel<DhabaModelMain>(
                                0,
                                it.message!!,
                                null
                            )
                        )
                    }
                    ApiResult.Status.SUCCESS -> {
//                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                        dialogProgressObserver.value = false
                        callBack.onResponse(it.data)
                    }
                }
            }
        }
    }

}