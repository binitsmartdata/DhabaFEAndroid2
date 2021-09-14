package com.transport.mall.model

import android.content.Context
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class OtherAmenitiesModel : Serializable {
    var service_id = ""
    var module_id = ""
    var dhaba_id = ""
    var mechanicShop: Int = 0
    var mechanicShopDay: Int = 0
    var punctureshop: Int = 0
    var punctureshopDay: Int = 0
    var dailyutilityshop: Int = 0
    var dailyutilityshopDay: Int = 0
    var barber: Int = 0
    var images: String = ""

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
//        if (policVerification == 1 && verificationImg.isEmpty()) {
//            callback.onResponse(false, "Please choose police verification photo.")
//        } else if (indoorCamera > 0 && indoorCameraImage.isEmpty()) {
//            callback.onResponse(false, "Please choose photos of indoor camera(s).")
//        } else if (outdoorCamera > 0 && outdoorCameraImage.isEmpty()) {
//            callback.onResponse(false, "Please choose photos of outdoor camera(s).")
//        } else {
        callback.onResponse(true, "")
//        }
    }
}