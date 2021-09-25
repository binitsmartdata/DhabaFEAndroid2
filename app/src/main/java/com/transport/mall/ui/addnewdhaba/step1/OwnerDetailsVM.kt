package com.transport.mall.ui.addnewdhaba.step1

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
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
    var progressObserverUpdate: MutableLiveData<Boolean> = MutableLiveData()

    var ownerModel = DhabaOwnerModel()

    init {
        app = application
    }

    fun addDhabaOwner(callBack: GenericCallBack<ApiResponseModel<DhabaOwnerModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addOwner(
                        RequestBody.create(MultipartBody.FORM, ownerModel._id),
                        RequestBody.create(MultipartBody.FORM, ownerModel.ownerName),
                        RequestBody.create(MultipartBody.FORM, ownerModel.mobilePrefix),
                        RequestBody.create(MultipartBody.FORM, ownerModel.mobile),
                        RequestBody.create(MultipartBody.FORM, ownerModel.email),
                        RequestBody.create(MultipartBody.FORM, ownerModel.address),
                        RequestBody.create(MultipartBody.FORM, ownerModel.panNumber),
                        RequestBody.create(MultipartBody.FORM, ownerModel.adharCard),
                        RequestBody.create(MultipartBody.FORM, ownerModel.latitude),
                        RequestBody.create(MultipartBody.FORM, ownerModel.longitude),
                        getMultipartImageFile(ownerModel.ownerPic, "ownerPic"),
                        getMultipartImageFile(ownerModel.idproofFront, "idproofFront"),
                        getMultipartImageFile(ownerModel.idproofBack, "idproofBack")
                    )
                ).collect {
                    handleResponse(it, callBack, progressObserver)
                }
            } catch (e: Exception) {
                progressObserver.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    fun updateOwner(callBack: GenericCallBack<ApiResponseModel<DhabaOwnerModel>>) {
        progressObserverUpdate.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.updateOwner(
                        RequestBody.create(MultipartBody.FORM, ownerModel._id),
                        RequestBody.create(MultipartBody.FORM, ownerModel.ownerName),
                        RequestBody.create(MultipartBody.FORM, ownerModel.mobilePrefix),
                        RequestBody.create(MultipartBody.FORM, ownerModel.mobile),
                        RequestBody.create(MultipartBody.FORM, ownerModel.email),
                        RequestBody.create(MultipartBody.FORM, ownerModel.address),
                        RequestBody.create(MultipartBody.FORM, ownerModel.panNumber),
                        RequestBody.create(MultipartBody.FORM, ownerModel.adharCard),
                        RequestBody.create(MultipartBody.FORM, ownerModel.latitude),
                        RequestBody.create(MultipartBody.FORM, ownerModel.longitude),
                        getMultipartImageFile(ownerModel.ownerPic, "profileImage"),
                        getMultipartImageFile(ownerModel.idproofFront, "idproofFront"),
                        getMultipartImageFile(ownerModel.idproofBack, "idproofBack")
                    )
                ).collect {
                    handleResponse(it, callBack, progressObserverUpdate)
                }
            } catch (e: Exception) {
                progressObserverUpdate.value = false
                showToastInCenter(app!!, getCorrectErrorMessage(e))
            }
        }
    }

    private fun handleResponse(
        it: ApiResult<ApiResponseModel<DhabaOwnerModel>>,
        callBack: GenericCallBack<ApiResponseModel<DhabaOwnerModel>>,
        observer: MutableLiveData<Boolean>
    ) {
        when (it.status) {
            ApiResult.Status.LOADING -> {
                observer.value = true
            }
            ApiResult.Status.ERROR -> {
                observer.value = false
                try {
                    callBack.onResponse(Gson().fromJson(it.error?.string(), ApiResponseModel::class.java) as ApiResponseModel<DhabaOwnerModel>?)
                } catch (e: Exception) {
                    callBack.onResponse(ApiResponseModel(0, it.message!!, null))
                }
            }
            ApiResult.Status.SUCCESS -> {
                observer.value = false
                callBack.onResponse(it.data)
            }
        }
    }
}