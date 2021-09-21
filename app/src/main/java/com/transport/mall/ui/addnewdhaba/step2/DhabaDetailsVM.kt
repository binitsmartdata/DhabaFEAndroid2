package com.transport.mall.ui.addnewdhaba.step2

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class DhabaDetailsVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var stateProgressObservable: MutableLiveData<Boolean> = MutableLiveData()
    var cityProgressObservable: MutableLiveData<Boolean> = MutableLiveData()
    var highwayProgressObservable: MutableLiveData<Boolean> = MutableLiveData()

    var dhabaModel: DhabaModel = DhabaModel()

    init {
        app = application
    }

    fun addDhaba(callBack: GenericCallBack<ApiResponseModel<DhabaModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.uploadDhabaDetails(
                    RequestBody.create(MultipartBody.FORM, dhabaModel.owner_id),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.name),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.address),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.landmark),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.area),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.highway),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.state),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.city),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.pincode),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.location),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.mobile),
                    RequestBody.create(MultipartBody.FORM, dhabaModel.propertyStatus),
                    RequestBody.create(MultipartBody.FORM, DhabaModel.STATUS_PENDING),
                    getMultipartImageFile(dhabaModel.images, "images"),
                    getMultipartVideoFile(dhabaModel.videos, "videos"),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData().id
                    ),
                    RequestBody.create(
                        MultipartBody.FORM,
                        SharedPrefsHelper.getInstance(app!!).getUserData().id
                    )
                )
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value =
                            true
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                        callBack.onResponse(ApiResponseModel<DhabaModel>(0, it.message!!, null))
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