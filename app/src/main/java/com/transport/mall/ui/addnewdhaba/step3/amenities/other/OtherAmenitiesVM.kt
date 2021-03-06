package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.OtherAmenitiesModel
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
class OtherAmenitiesVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var model = OtherAmenitiesModel()

    init {
        app = application
    }

    fun addOtherAmenities(callBack: GenericCallBack<ApiResponseModel<OtherAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addOtherAmenities(
                        RequestBody.create(MultipartBody.FORM, model.service_id),
                        RequestBody.create(MultipartBody.FORM, model.module_id),
                        RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                        RequestBody.create(MultipartBody.FORM, model.mechanicShop.toString()),
                        RequestBody.create(MultipartBody.FORM, model.mechanicShopType.toString()),
                        RequestBody.create(MultipartBody.FORM, model.punctureshop.toString()),
                        RequestBody.create(MultipartBody.FORM, model.punctureShopType.toString()),
                        RequestBody.create(MultipartBody.FORM, model.dailyutilityshop.toString()),
                        RequestBody.create(MultipartBody.FORM, model.utilityShopType.toString()),
                        RequestBody.create(MultipartBody.FORM, model.barber.toString()),
                        RequestBody.create(MultipartBody.FORM, model.barberShopType.toString()),
                        getMultipartImagesList(
                            model.barberImages,
                            "barberImages"
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

    fun updateOtherAmenities(callBack: GenericCallBack<ApiResponseModel<OtherAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.updateOtherAmenities(
                        RequestBody.create(MultipartBody.FORM, model._id),
                        RequestBody.create(MultipartBody.FORM, model.service_id),
                        RequestBody.create(MultipartBody.FORM, model.module_id),
                        RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                        RequestBody.create(MultipartBody.FORM, model.mechanicShop.toString()),
                        RequestBody.create(MultipartBody.FORM, model.mechanicShopType.toString()),
                        RequestBody.create(MultipartBody.FORM, model.punctureshop.toString()),
                        RequestBody.create(MultipartBody.FORM, model.punctureShopType.toString()),
                        RequestBody.create(MultipartBody.FORM, model.dailyutilityshop.toString()),
                        RequestBody.create(MultipartBody.FORM, model.utilityShopType.toString()),
                        RequestBody.create(MultipartBody.FORM, model.barber.toString()),
                        RequestBody.create(MultipartBody.FORM, model.barberShopType.toString()),
                        getMultipartImagesList(
                            model.barberImages,
                            "barberImages"
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
        it: ApiResult<ApiResponseModel<OtherAmenitiesModel>>,
        callBack: GenericCallBack<ApiResponseModel<OtherAmenitiesModel>>
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
                //                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                progressObserver.value = false
                callBack.onResponse(it.data)
            }
        }
    }

    fun delBarberImg(imgId: String, callBack: GenericCallBack<Boolean>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.delBarberImg(model._id, imgId)
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