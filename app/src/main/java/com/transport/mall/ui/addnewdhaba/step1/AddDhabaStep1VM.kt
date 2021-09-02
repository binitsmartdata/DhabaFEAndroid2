package com.transport.mall.ui.addnewdhaba.step1

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GenericCallBackTwoParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class AddDhabaStep1VM(application: Application) : BaseVM(application) {
    var app: Application? = null
    var progressObserver: MutableLiveData<Boolean> = MutableLiveData()

    init {
        app = application
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun refreshLocation() {
        progressObserver.value = true
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
}