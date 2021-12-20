package com.transport.mall.ui.addnewdhaba.step3.amenities.food

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.FoodAmenitiesModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class FoodAmenitiesVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

    var model = FoodAmenitiesModel()
    var dhabaModelMain = DhabaModelMain()

    init {
        app = application
    }

    fun addFoodAmenities(callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addFoodAmenities(
                        RequestBody.create(MultipartBody.FORM, model.service_id),
                        RequestBody.create(MultipartBody.FORM, model.module_id),
                        RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                        RequestBody.create(MultipartBody.FORM, GlobalUtils.getNonNullString(model.foodAt100, "")),
                        RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                        RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                        RequestBody.create(MultipartBody.FORM, GlobalUtils.getNonNullString(model.food, "")),
                        RequestBody.create(MultipartBody.FORM, model.foodLisence),
                        getMultipartImageFile(model.foodLisenceFile, "foodLisenceFile"),
                        getMultipartImagesList(model.images, "images")
                    )
                ).collect {
                    handleResponse(it, callBack)
                }
            } catch (e: Exception) {
                progressObserver.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun updateFoodAmenities(callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.updateFoodAmenities(
                        RequestBody.create(MultipartBody.FORM, model._id),
                        RequestBody.create(MultipartBody.FORM, model.service_id),
                        RequestBody.create(MultipartBody.FORM, model.module_id),
                        RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                        RequestBody.create(MultipartBody.FORM, GlobalUtils.getNonNullString(model.foodAt100, "")),
                        RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                        RequestBody.create(MultipartBody.FORM, model.roCleanWater),
                        RequestBody.create(MultipartBody.FORM, GlobalUtils.getNonNullString(model.food, "")),
                        RequestBody.create(MultipartBody.FORM, model.foodLisence),
                        getMultipartImageFile(model.foodLisenceFile, "foodLisenceFile"),
                        getMultipartImagesList(model.images, "images")
                    )
                ).collect {
                    handleResponse(it, callBack)
                }
            } catch (e: Exception) {
                progressObserver.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    private fun handleResponse(
        it: ApiResult<ApiResponseModel<FoodAmenitiesModel>>,
        callBack: GenericCallBack<ApiResponseModel<FoodAmenitiesModel>>
    ) {
        when (it.status) {
            ApiResult.Status.LOADING -> {
                progressObserver.value = true
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

    fun delFoodImg(imgId: String, callBack: GenericCallBack<Boolean>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.delFoodImg(model._id, imgId)
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
            } catch (e: Exception) {
                progressObserver.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }
}