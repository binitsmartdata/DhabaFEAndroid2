package com.transport.mall.ui.addnewdhaba.step1

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.DhabaModelMain
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

    var ownerName: ObservableField<String> = ObservableField()
    var mobile: ObservableField<String> = ObservableField()
    var email: ObservableField<String> = ObservableField()
    var address: ObservableField<String> = ObservableField()
    var location: ObservableField<String> = ObservableField()
    var panNumber: ObservableField<String> = ObservableField()
    var adharCard: ObservableField<String> = ObservableField()
    var ownerPic: ObservableField<String> = ObservableField()
    var idproofFront: ObservableField<String> = ObservableField()
    var idproofBack: ObservableField<String> = ObservableField()
    var dhaba_id: ObservableField<String> = ObservableField()
    var latitude: ObservableField<String> = ObservableField()
    var longitude: ObservableField<String> = ObservableField()

    init {
        app = application

        ownerName.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                ownerName.get()?.let { ownerModel.ownerName = it }
            }
        })
        mobile.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mobile.get()?.let { ownerModel.mobile = it.replace("[^\\d]", "") }
            }
        })
        email.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                email.get()?.let { ownerModel.email = it }
            }
        })
        address.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                address.get()?.let { ownerModel.address = it }
            }
        })
        location.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                location.get()?.let { ownerModel.location = it }
            }
        })
        latitude.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                latitude.get()?.let { ownerModel.latitude = it }
            }
        })
        longitude.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                longitude.get()?.let { ownerModel.longitude = it }
            }
        })
        panNumber.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                panNumber.get()?.let { ownerModel.panNumber = it }
            }
        })
        adharCard.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                adharCard.get()?.let { ownerModel.adharCard = it }
            }
        })
        ownerPic.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                ownerPic.get()?.let { ownerModel.ownerPic = it }
            }
        })
        idproofFront.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                idproofFront.get()?.let { ownerModel.idproofFront = it }
            }
        })
        idproofBack.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                idproofBack.get()?.let { ownerModel.idproofBack = it }
            }
        })
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