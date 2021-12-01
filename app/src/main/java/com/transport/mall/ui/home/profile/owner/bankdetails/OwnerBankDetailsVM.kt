package com.transport.mall.ui.home.profile.owner.bankdetails

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.BankDetailsModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class OwnerBankDetailsVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

    var bankModel = BankDetailsModel()

    var blockingMonths: ObservableField<String> = ObservableField()

    init {
        app = application
    }

    fun addBankDetail(callBack: GenericCallBack<ApiResponseModel<BankDetailsModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.addBankDetail(
                        RequestBody.create(MultipartBody.FORM, bankModel.user_id),
                        RequestBody.create(MultipartBody.FORM, bankModel.bankName),
                        RequestBody.create(MultipartBody.FORM, bankModel.gstNumber),
                        RequestBody.create(MultipartBody.FORM, bankModel.accountNumber),
                        RequestBody.create(MultipartBody.FORM, bankModel.ifscCode),
                        RequestBody.create(MultipartBody.FORM, bankModel.accountName),
                        RequestBody.create(MultipartBody.FORM, bankModel.panNumber),
                        getMultipartImageFile(bankModel.panPhoto, "panPhoto")
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

    fun updateBankDetail(callBack: GenericCallBack<ApiResponseModel<BankDetailsModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.updateBankDetail(
                        RequestBody.create(MultipartBody.FORM, bankModel._id),
                        RequestBody.create(MultipartBody.FORM, bankModel.user_id),
                        RequestBody.create(MultipartBody.FORM, bankModel.bankName),
                        RequestBody.create(MultipartBody.FORM, bankModel.gstNumber),
                        RequestBody.create(MultipartBody.FORM, bankModel.accountNumber),
                        RequestBody.create(MultipartBody.FORM, bankModel.ifscCode),
                        RequestBody.create(MultipartBody.FORM, bankModel.accountName),
                        RequestBody.create(MultipartBody.FORM, bankModel.panNumber),
                        getMultipartImageFile(bankModel.panPhoto, "panPhoto")
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
        it: ApiResult<ApiResponseModel<BankDetailsModel>>,
        callBack: GenericCallBack<ApiResponseModel<BankDetailsModel>>
    ) {
        when (it.status) {
            ApiResult.Status.LOADING -> {
                progressObserver.value = true
            }
            ApiResult.Status.ERROR -> {
                progressObserver.value = false
                callBack.onResponse(
                    ApiResponseModel<BankDetailsModel>(
                        0,
                        it.message!!,
                        null
                    )
                )
            }
            ApiResult.Status.SUCCESS -> {
                progressObserver.value = false
                callBack.onResponse(it.data!!)
            }
        }
    }
}