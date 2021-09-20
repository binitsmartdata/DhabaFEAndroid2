package com.transport.mall.ui.addnewdhaba.step2

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.ObservableField
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

    var name: ObservableField<String> = ObservableField()
    var owner_id: ObservableField<String> = ObservableField()
    var address: ObservableField<String> = ObservableField()
    var landmark: ObservableField<String> = ObservableField()
    var area: ObservableField<String> = ObservableField()
    var highway: ObservableField<String> = ObservableField()
    var state: ObservableField<String> = ObservableField()
    var city: ObservableField<String> = ObservableField()
    var pincode: ObservableField<String> = ObservableField()
    var location: ObservableField<String> = ObservableField()
    var mobile: ObservableField<String> = ObservableField()
    var propertyStatus: ObservableField<String> = ObservableField()
    var images: ObservableField<String> = ObservableField()
    var videos: ObservableField<String> = ObservableField()
    var latitude: ObservableField<String> = ObservableField()
    var longitude: ObservableField<String> = ObservableField()

    init {
        app = application

        owner_id.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                owner_id.get()?.let { dhabaModel.owner_id = it }
            }
        })
        name.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                name.get()?.let { dhabaModel.name = it }
            }
        })
        address.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                address.get()?.let { dhabaModel.address = it }
            }
        })
        landmark.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                landmark.get()?.let { dhabaModel.landmark = it }
            }
        })
        area.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                area.get()?.let { dhabaModel.area = it }
            }
        })
        highway.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                highway.get()?.let { dhabaModel.highway = it }
            }
        })
        state.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                state.get()?.let { dhabaModel.state = it }
            }
        })
        city.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                city.get()?.let { dhabaModel.city = it }
            }
        })
        pincode.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                pincode.get()?.let { dhabaModel.pincode = it }
            }
        })
        location.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                location.get()?.let { dhabaModel.location = it }
            }
        })
        mobile.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mobile.get()?.let { dhabaModel.mobile = it }
            }
        })
        propertyStatus.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                propertyStatus.get()?.let { dhabaModel.propertyStatus = it }
            }
        })
        images.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                images.get()?.let { dhabaModel.images = it }
            }
        })
        videos.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                videos.get()?.let { dhabaModel.videos = it }
            }
        })
        latitude.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                latitude.get()?.let { dhabaModel.latitude = it }
            }
        })
        longitude.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                longitude.get()?.let { dhabaModel.longitude = it }
            }
        })
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