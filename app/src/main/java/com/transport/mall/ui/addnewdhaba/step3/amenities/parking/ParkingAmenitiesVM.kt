package com.transport.mall.ui.addnewdhaba.step3.foodamenities

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.ParkingAmenitiesModel
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
import retrofit2.Response

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class ParkingAmenitiesVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var model: ParkingAmenitiesModel = ParkingAmenitiesModel()

    init {
        app = application
    }

    fun addParkingAmenities(callBack: GenericCallBack<ApiResponseModel<ParkingAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.addParkingAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.concreteParking),
                    RequestBody.create(MultipartBody.FORM, model.flatHardParking),
                    RequestBody.create(MultipartBody.FORM, model.kachaFlatParking),
                    RequestBody.create(MultipartBody.FORM, model.parkingSpace),
                    getMultipartImagesList(model.images, "images")
                )
            ).collect {
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
}