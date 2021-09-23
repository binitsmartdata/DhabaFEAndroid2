package com.transport.mall.ui.addnewdhaba.step3.foodamenities

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.ParkingAmenitiesModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
                handleResponse(it, callBack)
            }
        }
    }

    fun delParkingImg(imgId: String, callBack: GenericCallBack<Boolean>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.delParkingImg(model._id, imgId)
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value = true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                        callBack.onResponse(false)
                    }
                    ApiResult.Status.SUCCESS -> {
                        progressObserver.value = false
                        callBack.onResponse(true)
                    }
                }
            }
        }
    }

    fun updateParkingAmenities(callBack: GenericCallBack<ApiResponseModel<ParkingAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.updateParkingAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
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
                handleResponse(it, callBack)
            }
        }
    }

    private fun handleResponse(
        it: ApiResult<ApiResponseModel<ParkingAmenitiesModel>>,
        callBack: GenericCallBack<ApiResponseModel<ParkingAmenitiesModel>>
    ) {
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
                progressObserver.value = false
                callBack.onResponse(it.data)
            }
        }
    }
}