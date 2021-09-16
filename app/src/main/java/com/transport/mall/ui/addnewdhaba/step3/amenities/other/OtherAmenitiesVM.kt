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
            executeApi(
                getApiService()?.addOtherAmenities(
                    RequestBody.create(MultipartBody.FORM, model.service_id),
                    RequestBody.create(MultipartBody.FORM, model.module_id),
                    RequestBody.create(MultipartBody.FORM, model.dhaba_id),
                    RequestBody.create(MultipartBody.FORM, model.mechanicShop.toString()),
                    RequestBody.create(MultipartBody.FORM, model.mechanicShopDay.toString()),
                    RequestBody.create(MultipartBody.FORM, model.punctureshop.toString()),
                    RequestBody.create(MultipartBody.FORM, model.punctureshopDay.toString()),
                    RequestBody.create(MultipartBody.FORM, model.dailyutilityshop.toString()),
                    RequestBody.create(MultipartBody.FORM, model.dailyutilityshopDay.toString()),
                    RequestBody.create(MultipartBody.FORM, model.barber.toString()),
                    getMultipartImageFile(
                        model.barberImages,
                        "barberImages"
                    )!!
                )
            ).collect {
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
        }
    }

}