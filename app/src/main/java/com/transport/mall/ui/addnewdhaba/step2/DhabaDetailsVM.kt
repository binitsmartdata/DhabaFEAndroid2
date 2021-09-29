package com.transport.mall.ui.addnewdhaba.step2

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
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
    var progressObserverUpdate: MutableLiveData<Boolean> = MutableLiveData()

    var dhabaModel: DhabaModel = DhabaModel()

    init {
        app = application
    }

    fun addDhaba(isDraft: Boolean, callBack: GenericCallBack<ApiResponseModel<DhabaModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addDhaba(
                        RequestBody.create(MultipartBody.FORM, dhabaModel.owner_id),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.name),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.address),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.landmark),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.area),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.highway),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.state),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.city),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.pincode),
                        RequestBody.create(
                            MultipartBody.FORM,
                            dhabaModel.latitude + "," + dhabaModel.longitude
                        ),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.mobile),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.propertyStatus),
                        RequestBody.create(MultipartBody.FORM, if (isDraft) DhabaModel.STATUS_PENDING else DhabaModel.STATUS_INPROGRESS),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.latitude),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.longitude),
                        getMultipartImageFile(dhabaModel.images, "images"),
                        getMultipartVideoFile(dhabaModel.videos, "videos"),
                        RequestBody.create(
                            MultipartBody.FORM,
                            SharedPrefsHelper.getInstance(app!!).getUserData()._id
                        ),
                        RequestBody.create(
                            MultipartBody.FORM,
                            SharedPrefsHelper.getInstance(app!!).getUserData()._id
                        )
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

    fun updateDhaba(isDraft: Boolean, callBack: GenericCallBack<ApiResponseModel<DhabaModel>>) {
        progressObserverUpdate.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.updateDhaba(
                        RequestBody.create(MultipartBody.FORM, dhabaModel._id),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.owner_id),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.name),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.address),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.landmark),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.area),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.highway),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.state),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.city),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.pincode),
                        RequestBody.create(
                            MultipartBody.FORM,
                            dhabaModel.latitude + "," + dhabaModel.longitude
                        ),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.mobile),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.propertyStatus),
                        RequestBody.create(MultipartBody.FORM, if (isDraft) DhabaModel.STATUS_PENDING else DhabaModel.STATUS_INPROGRESS),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.latitude),
                        RequestBody.create(MultipartBody.FORM, dhabaModel.longitude),
                        getMultipartImageFile(dhabaModel.images, "images"),
                        getMultipartVideoFile(dhabaModel.videos, "videos"),
                        RequestBody.create(
                            MultipartBody.FORM,
                            SharedPrefsHelper.getInstance(app!!).getUserData()._id
                        ),
                        RequestBody.create(
                            MultipartBody.FORM,
                            SharedPrefsHelper.getInstance(app!!).getUserData()._id
                        )
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
        it: ApiResult<ApiResponseModel<DhabaModel>>,
        callBack: GenericCallBack<ApiResponseModel<DhabaModel>>,
        observer: MutableLiveData<Boolean>
    ) {
        when (it.status) {
            ApiResult.Status.LOADING -> {
                observer.value = true
            }
            ApiResult.Status.ERROR -> {
                observer.value = false
                try {
                    callBack.onResponse(
                        Gson().fromJson(
                            it.error?.string(),
                            ApiResponseModel::class.java
                        ) as ApiResponseModel<DhabaModel>?
                    )
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