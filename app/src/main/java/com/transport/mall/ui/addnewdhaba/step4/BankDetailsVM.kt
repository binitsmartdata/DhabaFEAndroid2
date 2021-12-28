package com.transport.mall.ui.addnewdhaba.step1

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.BankDetailsModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.UserModel
import com.transport.mall.model.UserModelMain
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
class BankDetailsVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

    var progressObserverOwner: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverDhaba: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverDhabaTiming: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverFood: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverParking: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverSleeping: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverWashroom: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverSecurity: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverLight: MutableLiveData<Boolean> = MutableLiveData()
    var progressObserverOther: MutableLiveData<Boolean> = MutableLiveData()

    var bankModel = BankDetailsModel()
    var dhabaModel = DhabaModel()

    var blockingMonths: ObservableField<String> = ObservableField()

    init {
        app = application

        blockingMonths.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                blockingMonths.get()
                    ?.let { dhabaModel.blockMonth = GlobalUtils.getNonNullString(it, "0").toInt() }
            }
        })
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
                try {
                    callBack.onResponse(
                        Gson().fromJson(
                            it.error?.string(),
                            ApiResponseModel::class.java
                        ) as ApiResponseModel<BankDetailsModel>?
                    )
                } catch (e: Exception) {
                    callBack.onResponse(ApiResponseModel(0, it.message!!, null))
                }
            }
            ApiResult.Status.SUCCESS -> {
                progressObserver.value = false
                callBack.onResponse(it.data!!)
            }
        }
    }
}