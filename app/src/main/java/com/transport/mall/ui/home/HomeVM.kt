package com.transport.mall.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.AppDatabase
import com.transport.mall.model.ApiResponseModel
import com.transport.mall.model.CityModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.NetworkAdapter
import com.transport.mall.utils.base.BaseVM
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
class HomeVM(application: Application) : BaseVM(application) {
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

    fun getCitiesList() {
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
                        progressObserver.value = false
//                        AppDatabase.getInstance(app!!)?.cityDao?.insertAll(it.data?.data)
                    }
                }
            }
        }
    }

    suspend fun getAllCities(): Flow<ApiResult<ApiResponseModel<ArrayList<CityModel>>>> {
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