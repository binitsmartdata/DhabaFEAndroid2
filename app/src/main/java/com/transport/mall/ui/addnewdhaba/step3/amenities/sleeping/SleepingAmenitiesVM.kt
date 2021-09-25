package com.transport.mall.ui.addnewdhaba.step3.foodamenities

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.SleepingAmenitiesModel
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
class SleepingAmenitiesVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var model: SleepingAmenitiesModel = SleepingAmenitiesModel()

    init {
        app = application
    }

    fun addSleepingAmenities(callBack: GenericCallBack<ApiResponseModel<SleepingAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addSleepingAmenities(
                        RequestBody.create(MultipartBody.FORM, model.service_id),
                        RequestBody.create(MultipartBody.FORM, model.module_id),
                        RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                        RequestBody.create(MultipartBody.FORM, model.sleeping),
                        RequestBody.create(MultipartBody.FORM, model.noOfBeds),
                        RequestBody.create(MultipartBody.FORM, model.fan),
                        RequestBody.create(MultipartBody.FORM, model.cooler),
                        RequestBody.create(MultipartBody.FORM, model.enclosed),
                        RequestBody.create(MultipartBody.FORM, model.open),
                        RequestBody.create(MultipartBody.FORM, model.hotWater),
                        getMultipartImageFile(model.images, "images")
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
                            progressObserver.value = false
                            callBack.onResponse(it.data)
                        }
                    }
                }
            } catch (e: Exception) {
                progressObserver.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun updateSleepingAmenities(callBack: GenericCallBack<ApiResponseModel<SleepingAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.updateSleepingAmenities(
                        RequestBody.create(MultipartBody.FORM, model._id),
                        RequestBody.create(MultipartBody.FORM, model.service_id),
                        RequestBody.create(MultipartBody.FORM, model.module_id),
                        RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                        RequestBody.create(MultipartBody.FORM, model.sleeping),
                        RequestBody.create(MultipartBody.FORM, model.noOfBeds),
                        RequestBody.create(MultipartBody.FORM, model.fan),
                        RequestBody.create(MultipartBody.FORM, model.cooler),
                        RequestBody.create(MultipartBody.FORM, model.enclosed),
                        RequestBody.create(MultipartBody.FORM, model.open),
                        RequestBody.create(MultipartBody.FORM, model.hotWater),
                        getMultipartImageFile(model.images, "images")
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
                            progressObserver.value = false
                            callBack.onResponse(it.data)
                        }
                    }
                }
            } catch (e: Exception) {
                progressObserver.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }
}