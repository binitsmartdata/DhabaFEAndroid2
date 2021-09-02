package com.transport.mall.ui.addnewdhaba.step1

import android.annotation.SuppressLint
import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.transport.mall.model.LocationAddressModel
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import java.util.*


/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class AddDhabaStep2VM(application: Application) : BaseVM(application) {
    var app: Application? = null

    init {
        app = application
    }

}