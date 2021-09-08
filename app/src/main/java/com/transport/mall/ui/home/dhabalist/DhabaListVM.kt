package com.transport.mall.ui.home.dhabalist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.AppDatabase
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.model.DhabaModel
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

    var app: Application? = null

    init {
        app = application
    }

    fun observerError(): MutableLiveData<String>? {
        errorResponse = null
        errorResponse = MutableLiveData()
        return errorResponse
    }

    fun getCitiesList(callBack: GenericCallBack<ArrayList<CityAndStateModel>>) {
        GlobalScope.launch(Dispatchers.Main) {
            getAllCities(app!!).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value = true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                    }
                    ApiResult.Status.SUCCESS -> {
                        AppDatabase.getInstance(app!!)?.cityDao()
                            ?.insertAll(it.data?.data?.data as List<CityAndStateModel>)
                        progressObserver.value = false
                        callBack.onResponse(it.data?.data?.data)
                    }
                }
            }
        }
    }

    fun getAllDhabaList(
        limit: String,
        page: String,
        callBack: GenericCallBack<ArrayList<DhabaModel>>
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            super.getAllDhabaList(app!!, limit, page).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value = page == "1"
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                    }
                    ApiResult.Status.SUCCESS -> {
                        progressObserver.value = false
                        callBack.onResponse(it.data?.data?.data)
                    }
                }
            }
        }
    }
}