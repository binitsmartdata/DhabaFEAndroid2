package com.transport.mall.ui.addnewdhaba.step3.foodamenities

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.SleepingAmenitiesModel
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
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class SleepingAmenitiesVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var model: SleepingAmenitiesModel = SleepingAmenitiesModel()

    init {
        app = application
    }

    fun addParkingAmenities(callBack: GenericCallBack<ApiResponseModel<SleepingAmenitiesModel>>) {
        GlobalScope.launch(Dispatchers.Main) {
            uploadSleepingAmenities().collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value =
                            true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                        callBack.onResponse(
                            ApiResponseModel(
                                0,
                                it.message!!,
                                null
                            )
                        )
                    }
                    ApiResult.Status.SUCCESS -> {
//                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                        progressObserver.value = false
                        callBack.onResponse(it.data)
                    }
                }
            }
        }
    }

    suspend fun uploadSleepingAmenities(): Flow<ApiResult<ApiResponseModel<SleepingAmenitiesModel>>> {
        return flow {
            emit(ApiResult.loading())
            emit(
                getResponse(
                    request = {
                        NetworkAdapter.getInstance().getNetworkServices()?.addSleepingAmenities(
                            RequestBody.create(MultipartBody.FORM, "1"),
                            RequestBody.create(MultipartBody.FORM, "1"),
                            RequestBody.create(MultipartBody.FORM, "6137443bb5828a682d08ecf1"),
                            RequestBody.create(MultipartBody.FORM, model.sleeping),
                            RequestBody.create(MultipartBody.FORM, model.fan),
                            RequestBody.create(MultipartBody.FORM, model.enclosed),
                            RequestBody.create(MultipartBody.FORM, model.open),
                            RequestBody.create(MultipartBody.FORM, model.hotWater)
                        )
                    }
                )
            )
        }.flowOn(Dispatchers.IO)
    }
}