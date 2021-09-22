package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.WashroomAmenitiesModel
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
class WashroomAmenitiesVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var model = WashroomAmenitiesModel()

    init {
        app = application
    }

    fun addWashroomAmenities(callBack: GenericCallBack<ApiResponseModel<WashroomAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.addWashroomAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.washroomStatus),
                    RequestBody.create(MultipartBody.FORM, model.water),
                    RequestBody.create(MultipartBody.FORM, model.cleaner),
                    getMultipartImageFile(model.images, "images")
                )
            ).collect {
                handleResult(it, callBack)
            }
        }
    }

    fun updatewashroomAmenities(callBack: GenericCallBack<ApiResponseModel<WashroomAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.updatewashroomAmenities(
                    RequestBody.create(MultipartBody.FORM, model._id),
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.washroomStatus),
                    RequestBody.create(MultipartBody.FORM, model.water),
                    RequestBody.create(MultipartBody.FORM, model.cleaner),
                    getMultipartImageFile(model.images, "images")
                )
            ).collect {
                handleResult(it, callBack)
            }
        }
    }

    private fun handleResult(
        it: ApiResult<ApiResponseModel<WashroomAmenitiesModel>>,
        callBack: GenericCallBack<ApiResponseModel<WashroomAmenitiesModel>>
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
                //                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                progressObserver.value = false
                callBack.onResponse(it.data)
            }
        }
    }

}