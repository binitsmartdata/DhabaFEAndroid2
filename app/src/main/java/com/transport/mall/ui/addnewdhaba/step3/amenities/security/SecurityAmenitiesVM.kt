package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.SecurityAmenitiesModel
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
class SecurityAmenitiesVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var model = SecurityAmenitiesModel()

    init {
        app = application
    }

    fun addSecurityAmenities(callBack: GenericCallBack<ApiResponseModel<SecurityAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addSecurityAmenities(
                        RequestBody.create(MultipartBody.FORM, model.service_id),
                        RequestBody.create(MultipartBody.FORM, model.module_id),
                        RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                        RequestBody.create(MultipartBody.FORM, model.dayGuard.toString()),
                        RequestBody.create(MultipartBody.FORM, model.nightGuard.toString()),
                        RequestBody.create(MultipartBody.FORM, model.policVerification.toString()),
                        getMultipartImageFile(
                            model.verificationImg,
                            "verificationImg"
                        ),
                        RequestBody.create(MultipartBody.FORM, model.indoorCamera.toString()),
                        getMultipartImagesList(
                            model.indoorCameraImage,
                            "indoorCameraImage"
                        ),
                        RequestBody.create(MultipartBody.FORM, model.outdoorCamera.toString()),
                        getMultipartImagesList(
                            model.outdoorCameraImage,
                            "outdoorCameraImage"
                        )
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

    fun updateSecurityAmenities(callBack: GenericCallBack<ApiResponseModel<SecurityAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.updateSecurityAmenities(
                        RequestBody.create(MultipartBody.FORM, model._id),
                        RequestBody.create(MultipartBody.FORM, model.service_id),
                        RequestBody.create(MultipartBody.FORM, model.module_id),
                        RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                        RequestBody.create(MultipartBody.FORM, model.dayGuard.toString()),
                        RequestBody.create(MultipartBody.FORM, model.nightGuard.toString()),
                        RequestBody.create(MultipartBody.FORM, model.policVerification.toString()),
                        getMultipartImageFile(
                            model.verificationImg,
                            "verificationImg"
                        ),
                        RequestBody.create(MultipartBody.FORM, model.indoorCamera.toString()),
                        getMultipartImagesList(
                            model.indoorCameraImage,
                            "indoorCameraImage"
                        ),
                        RequestBody.create(MultipartBody.FORM, model.outdoorCamera.toString()),
                        getMultipartImagesList(
                            model.outdoorCameraImage,
                            "outdoorCameraImage"
                        )
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
        it: ApiResult<ApiResponseModel<SecurityAmenitiesModel>>,
        callBack: GenericCallBack<ApiResponseModel<SecurityAmenitiesModel>>
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

    fun delSecurityImg(indoorCameraImageId: String?, outdoorCameraImageId: String?, callBack: GenericCallBack<Boolean>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.delSecurityImg(model._id, indoorCameraImageId, outdoorCameraImageId)
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