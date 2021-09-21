package com.transport.mall.ui.addnewdhaba.step1

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.DhabaOwnerModel
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
class OwnerDetailsVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

    var ownerModel = DhabaOwnerModel()

    init {
        app = application
    }

    fun addDhabaOwner(callBack: GenericCallBack<ApiResponseModel<DhabaOwnerModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.addOwner(
                    RequestBody.create(MultipartBody.FORM, ownerModel._id),
                    RequestBody.create(MultipartBody.FORM, ownerModel.ownerName),
                    RequestBody.create(MultipartBody.FORM, ownerModel.mobile),
                    RequestBody.create(MultipartBody.FORM, ownerModel.email),
                    RequestBody.create(MultipartBody.FORM, ownerModel.address),
                    RequestBody.create(MultipartBody.FORM, ownerModel.panNumber),
                    RequestBody.create(MultipartBody.FORM, ownerModel.adharCard),
                    getMultipartImageFile(ownerModel.ownerPic, "ownerPic"),
                    getMultipartImageFile(ownerModel.idproofFront, "idproofFront"),
                    getMultipartImageFile(ownerModel.idproofBack, "idproofBack")
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
                            ApiResponseModel<DhabaOwnerModel>(
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