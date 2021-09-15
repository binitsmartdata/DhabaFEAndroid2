package com.transport.mall.ui.addnewdhaba.step2

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.LocationAddressModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class DhabaDetailsVM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()
    var stateProgressObservable: MutableLiveData<Boolean> = MutableLiveData()
    var cityProgressObservable: MutableLiveData<Boolean> = MutableLiveData()

    private var dhabaModel: DhabaModel = DhabaModel()

    var name: ObservableField<String> = ObservableField()
    var ownerName: ObservableField<String> = ObservableField()
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

    init {
        app = application
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun refreshLocation() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(app)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(callBack: GenericCallBack<Location>) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                callBack.onResponse(location)
            }
    }

    fun getAddressUsingLatLong(latitude: Double, longitude: Double): LocationAddressModel {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(app, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        val address: String =
            addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city: String = addresses[0].getLocality()
        val state: String = addresses[0].getAdminArea()
        val country: String = addresses[0].getCountryName()
        val postalCode: String = addresses[0].getPostalCode()
        val knownName: String = addresses[0].getFeatureName() // Only if available else return NULL

        return LocationAddressModel(address, city, state, country, postalCode, knownName)
//        return address
    }

    fun getCitiesByStateId(
        stateId: String,
        callBack: GenericCallBack<ArrayList<CityAndStateModel>>
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.getCitiesByState(stateId)
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        cityProgressObservable.value = true
                    }
                    ApiResult.Status.ERROR -> {
                        cityProgressObservable.value = false
                    }
                    ApiResult.Status.SUCCESS -> {
                        cityProgressObservable.value = false
                        callBack.onResponse(it.data?.data)
                    }
                }
            }
        }
    }

    fun getStatesList(
        callBack: GenericCallBack<ArrayList<CityAndStateModel>>
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.getAllStates(
                    getAccessToken(app!!),
                    "999",
                    "", "", "1", "ASC", "true"
                )
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        stateProgressObservable.value = true
                    }
                    ApiResult.Status.ERROR -> {
                        stateProgressObservable.value = false
                    }
                    ApiResult.Status.SUCCESS -> {
//                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                        stateProgressObservable.value = false
                        callBack.onResponse(it.data?.data?.data)
                    }
                }
            }
        }
    }

    fun addDhaba(callBack: GenericCallBack<ApiResponseModel<DhabaModel>>) {
        progressObserver.value = true
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.uploadDhabaDetails(
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().name),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().address),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().landmark),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().area),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().highway),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().state),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().city),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().pincode),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().location),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().mobile),
                    RequestBody.create(MultipartBody.FORM, getDhabaModel().propertyStatus),
                    RequestBody.create(MultipartBody.FORM, DhabaModel.STATUS_PENDING),
                    MultipartBody.Part.createFormData(
                        "images",
                        File(getDhabaModel().images).getName(),
                        RequestBody.create(
                            MediaType.parse("image/*"), getDhabaModel().images
                        )
                    ),
                    if (getDhabaModel().videos.isNotEmpty()) MultipartBody.Part.createFormData(
                        "videos",
                        File(getDhabaModel().videos).getName(),
                        RequestBody.create(
                            MediaType.parse("video/*"), getDhabaModel().videos
                        )
                    ) else null,
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

    fun getDhabaModel(): DhabaModel {
        name.get()?.let { dhabaModel.name = it }
        address.get()?.let { dhabaModel.address = it }
        landmark.get()?.let { dhabaModel.landmark = it }
        area.get()?.let { dhabaModel.area = it }
        highway.get()?.let { dhabaModel.highway = it }
        state.get()?.let { dhabaModel.state = it }
        city.get()?.let { dhabaModel.city = it }
        pincode.get()?.let { dhabaModel.pincode = it }
        location.get()?.let { dhabaModel.location = it }
        mobile.get()?.let { dhabaModel.mobile = it }
        propertyStatus.get()?.let { dhabaModel.propertyStatus = it }
        images.get()?.let { dhabaModel.images = it }
        videos.get()?.let { dhabaModel.videos = it }

        return dhabaModel
    }

}