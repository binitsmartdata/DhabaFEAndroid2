package com.transport.mall.ui.addnewdhaba.step1

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.BankDetailsModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class Step4BankDetailsVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

    var bankModel = BankDetailsModel()

    var user_id: ObservableField<String> = ObservableField()
    var bankName: ObservableField<String> = ObservableField()
    var gstNumber: ObservableField<String> = ObservableField()
    var ifscCode: ObservableField<String> = ObservableField()
    var accountName: ObservableField<String> = ObservableField()
    var panNumber: ObservableField<String> = ObservableField()
    var panPhoto: ObservableField<String> = ObservableField()

    init {
        app = application

        user_id.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                user_id.get()?.let { bankModel.user_id = it }
            }
        })
        bankName.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                bankName.get()?.let { bankModel.bankName = it }
            }
        })
        gstNumber.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                gstNumber.get()?.let { bankModel.gstNumber = it }
            }
        })
        ifscCode.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                ifscCode.get()?.let { bankModel.ifscCode = it }
            }
        })
        accountName.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                accountName.get()?.let { bankModel.accountName = it }
            }
        })
        panNumber.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                panNumber.get()?.let { bankModel.panNumber = it }
            }
        })
        panPhoto.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                panPhoto.get()?.let { bankModel.panPhoto = it }
            }
        })
    }

    fun addBankDetail(callBack: GenericCallBack<ApiResponseModel<BankDetailsModel>>) {
        GlobalScope.launch(Dispatchers.Main) {
            super.addBankDetail(bankModel).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value =
                            true
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
//                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                        progressObserver.value = false
                        callBack.onResponse(it.data!!)
                    }
                }
            }
        }
    }
}