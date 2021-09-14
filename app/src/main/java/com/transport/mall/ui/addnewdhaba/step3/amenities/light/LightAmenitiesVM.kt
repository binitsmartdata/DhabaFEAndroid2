package com.transport.mall.ui.addnewdhaba.step3.amenities.sleeping

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.LightAmenitiesModel
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
class LightAmenitiesVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var model = LightAmenitiesModel()

    init {
        app = application
    }

    fun addLightAmenities(callBack: GenericCallBack<ApiResponseModel<LightAmenitiesModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.addLightAmenities(
                    RequestBody.create(MultipartBody.FORM, "1"),
                    RequestBody.create(MultipartBody.FORM, "1"),
                    RequestBody.create(MultipartBody.FORM, "6137443bb5828a682d08ecf1"),
                    RequestBody.create(MultipartBody.FORM, model.tower_light.toString()),
                    getMultipartImageFile(model.tower_image, "tower_image"),
                    RequestBody.create(MultipartBody.FORM, model.bulb_light.toString()),
                    getMultipartImageFile(model.bulb_image, "buld_image"),
                    RequestBody.create(
                        MultipartBody.FORM,
                        model.twentyfour_seven_electricity.toString()
                    )
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

    fun uploadImage(
        path: String,
        callBack: GenericCallBack<String>
    ) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.uploadImg(getMultipartImageFile(path, "images"))
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value = true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                        callBack.onResponse(it.message)
                    }
                    ApiResult.Status.SUCCESS -> {
//                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                        progressObserver.value = false
                        callBack.onResponse(it.data?.string())
                    }
                }
            }
        }
    }

}