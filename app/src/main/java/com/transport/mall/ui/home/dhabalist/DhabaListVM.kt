package com.transport.mall.ui.home.dhabalist

import android.app.Application
import androidx.lifecycle.MutableLiveData
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
        token: String,
        limit: String,
        page: String,
        cities: String,
        search: String,
        status: String,
        callBack: GenericCallBack<ArrayList<DhabaModelMain>>
    ) {
        progressObserver.value = page == "1"
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(getApiService()?.getAllDhabaList(token, limit, page, status, cities, search)).collect {
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
            } catch (e: Exception) {
                progressObserver.value = false
//                showToastInCenter(app!!, getCorrectErrorMessage(e))
                callBack.onResponse(ArrayList())
            }
        }
    }
}