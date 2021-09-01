package com.transport.mall.ui.home.dhabalist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.InternalDataListModel
import com.transport.mall.model.CityModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.NetworkAdapter
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * Created by Vishal Sharma on 2019-12-06.
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

    fun getCitiesList(callBack: GenericCallBack<ArrayList<CityModel>>) {
        GlobalScope.launch(Dispatchers.Main) {
            getAllCities().collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value = true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                    }
                    ApiResult.Status.SUCCESS -> {
//                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                        progressObserver.value = false
                        callBack.onResponse(it.data?.data?.data)
                    }
                }
            }
        }
    }

    suspend fun getAllCities(): Flow<ApiResult<ApiResponseModel<InternalDataListModel<ArrayList<CityModel>>>>> {
        return flow {
            emit(ApiResult.loading())
            emit(
                getResponse(
                    request = {
                        NetworkAdapter.getInstance().getNetworkServices()?.getAllCities(
                            getAccessToken(app!!),
                            "10",
                            "", "", "999", "ASC", "true"
                        )
                    }
                )
            )
        }.flowOn(Dispatchers.IO)
    }
}