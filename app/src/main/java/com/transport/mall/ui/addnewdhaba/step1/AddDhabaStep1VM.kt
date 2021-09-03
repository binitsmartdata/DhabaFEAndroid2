package com.transport.mall.ui.addnewdhaba.step1

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
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.NetworkAdapter
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class AddDhabaStep1VM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

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

    fun getAddressUsingLatLong(latitude: Double, longitude: Double): String {
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

//        return LocationAddressModel(city, state, country, postalCode, knownName)
        return address
    }

    fun getCitiesList(callBack: GenericCallBackTwoParams<ArrayList<CityAndStateModel>, ArrayList<CityAndStateModel>>) {
        GlobalScope.launch(Dispatchers.Main) {
            getAllCities(app!!).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
                        progressObserver.value = true
                    }
                    ApiResult.Status.ERROR -> {
//                        hideProgressDialog()
                    }
                    ApiResult.Status.SUCCESS -> {
//                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
//                        hideProgressDialog()
                        getStatesList(it.data?.data?.data!!, callBack)
                    }
                }
            }
        }
    }

    fun getStatesList(
        citiesList: ArrayList<CityAndStateModel>,
        callBack: GenericCallBackTwoParams<ArrayList<CityAndStateModel>, ArrayList<CityAndStateModel>>
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            getAllStates(app!!).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {
//                        showProgressDialog()
                    }
                    ApiResult.Status.ERROR -> {
                        progressObserver.value = false
                    }
                    ApiResult.Status.SUCCESS -> {
//                        AppDatabase.getInstance(app!!)?.cityDao()?.insertAll(it.data?.data?.data as List<CityModel>)
                        progressObserver.value = false
                        callBack.onResponse(citiesList, it.data?.data?.data)
                    }
                }
            }
        }
    }

    fun addDhaba(
        name: String,
        address: String,
        landmark: String,
        area: String,
        highway: String,
        state: String,
        city: String,
        pincode: String,
        location: String,
        mobile: String,
        propertyStatus: String,
        images: File,
        videos: File,
        createdBy: String,
        updatedBy: String,
        callBack: GenericCallBack<ApiResponseModel<DhabaModel>>
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            uploadDhabaDetails(
                app!!,
                name,
                address,
                landmark,
                area,
                highway,
                state,
                city,
                pincode,
                location,
                mobile,
                propertyStatus,
                images,
                videos,
                createdBy,
                updatedBy
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

    suspend fun uploadDhabaDetails(
        app: Application,
        name: String,
        address: String,
        landmark: String,
        area: String,
        highway: String,
        state: String,
        city: String,
        pincode: String,
        location: String,
        mobile: String,
        propertyStatus: String,
        images: File,
        videos: File,
        createdBy: String,
        updatedBy: String
    ): Flow<ApiResult<ApiResponseModel<DhabaModel>>> {
        return flow {
            emit(ApiResult.loading())
            emit(
                getResponse(
                    request = {
                        NetworkAdapter.getInstance().getNetworkServices()?.uploadDhabaDetails(
                            RequestBody.create(MultipartBody.FORM, name),
                            RequestBody.create(MultipartBody.FORM, address),
                            RequestBody.create(MultipartBody.FORM, landmark),
                            RequestBody.create(MultipartBody.FORM, area),
                            RequestBody.create(MultipartBody.FORM, highway),
                            RequestBody.create(MultipartBody.FORM, state),
                            RequestBody.create(MultipartBody.FORM, city),
                            RequestBody.create(MultipartBody.FORM, pincode),
                            RequestBody.create(MultipartBody.FORM, location),
                            RequestBody.create(MultipartBody.FORM, mobile),
                            RequestBody.create(MultipartBody.FORM, propertyStatus),
                            MultipartBody.Part.createFormData(
                                "images", images.getName(), RequestBody.create(
                                    MediaType.parse("image/*"), images
                                )
                            ),
                            MultipartBody.Part.createFormData(
                                "videos", videos.getName(), RequestBody.create(
                                    MediaType.parse("video/*"), videos
                                )
                            ),
                            RequestBody.create(MultipartBody.FORM, createdBy),
                            RequestBody.create(MultipartBody.FORM, updatedBy)
                        )
                    }
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    fun getDhabaModel(): DhabaModel {
        name.get()?.let { dhabaModel.name = it }
        ownerName.get()?.let { dhabaModel.ownerName = it }
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