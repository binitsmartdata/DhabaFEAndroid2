package com.transport.mall.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class OtherAmenitiesModel : Serializable {
    @SerializedName(value = "_id")
    var _id = ""

    @SerializedName(value = "service_id")
    var service_id = "1"

    @SerializedName(value = "module_id")
    var module_id = "1"

    @SerializedName(value = "dhaba_id")
    var dhaba_id = ""

    @SerializedName(value = "mechanicShop")
    var mechanicShop: Int = 0

    @SerializedName(value = "mechanicShopDay")
    var mechanicShopDay: Int = 0

    @SerializedName(value = "punctureshop")
    var punctureshop: Int = 0

    @SerializedName(value = "punctureshopDay")
    var punctureshopDay: Int = 0

    @SerializedName(value = "dailyutilityshop")
    var dailyutilityshop: Int = 0

    @SerializedName(value = "dailyutilityshopDay")
    var dailyutilityshopDay: Int = 0

    @SerializedName(value = "barber")
    var barber: Int = 0

    @SerializedName(value = "barberImages")
    var barberImages: String = ""

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