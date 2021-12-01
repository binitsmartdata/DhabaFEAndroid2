package com.transport.mall.ui.home.profile.owner

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.UserModel
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
class ProfileVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverUpdate: MutableLiveData<Boolean> = MutableLiveData()

    var userModel = UserModel()

    init {
        app = application
    }

    fun updateUserProfile(callBack: GenericCallBack<ApiResponseModel<UserModel>>) {
        progressObserverUpdate.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.updateUserProfile(
                        RequestBody.create(MultipartBody.FORM, userModel._id),
                        RequestBody.create(MultipartBody.FORM, userModel.ownerName),
                        RequestBody.create(MultipartBody.FORM, userModel.email),
                        RequestBody.create(MultipartBody.FORM, userModel.mobile),
                        RequestBody.create(MultipartBody.FORM, userModel.mobilePrefix),
                        getMultipartImageFile(userModel.ownerPic, "profileImage")
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

    fun removeProfileImage(callBack: GenericCallBack<ApiResponseModel<UserModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.removeProfileImage(
                        RequestBody.create(MultipartBody.FORM, userModel._id),
                        RequestBody.create(MultipartBody.FORM, "")
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

    private fun handleResponse(
        it: ApiResult<ApiResponseModel<UserModel>>,
        callBack: GenericCallBack<ApiResponseModel<UserModel>>,
        observer: MutableLiveData<Boolean>
    ) {
        when (it.status) {
            ApiResult.Status.LOADING -> {
                observer.value = true
            }
            ApiResult.Status.ERROR -> {
                observer.value = false
                try {
                    callBack.onResponse(Gson().fromJson(it.error?.string(), ApiResponseModel::class.java) as ApiResponseModel<UserModel>?)
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