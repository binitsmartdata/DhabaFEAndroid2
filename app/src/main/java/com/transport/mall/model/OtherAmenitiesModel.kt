package com.transport.mall.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
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

    @SerializedName(value = "mechanic")
    var mechanic: Boolean = false
    @SerializedName(value = "mechanicShop")
    var mechanicShop: Int = 0
    @SerializedName(value = "mechanicShopType")
    var mechanicShopType: Int = 0
    var mechanicShopEnabled: Boolean = false

    @SerializedName(value = "puncture")
    var puncture: Boolean = false
    @SerializedName(value = "punctureshop")
    var punctureshop: Int = 0
    @SerializedName(value = "punctureShopType")
    var punctureShopType: Int = 0
    var punctureshopEnabled: Boolean = false

    @SerializedName(value = "dailyutility")
    var dailyutility: Boolean = false
    @SerializedName(value = "dailyutilityshop")
    var dailyutilityshop: Int = 0
    @SerializedName(value = "utilityShopType")
    var utilityShopType: Int = 0
    var dailyutilityEnabled: Boolean = false

    @SerializedName(value = "barberDis")
    var barberDis: Boolean = false
    @SerializedName(value = "barber")
    var barber: Int = 0
    @SerializedName(value = "barberShopType")
    var barberShopType: Int = 0
    var barberEnabled: Boolean = false

    @SerializedName(value = "barberImages")
    var barberImages: ArrayList<PhotosModel> = ArrayList()

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (mechanicShopEnabled && (mechanicShop == 0 || mechanicShopType == 0)) {
            callback.onResponse(false, context.getString(R.string.choose_mechanic))
        } else if (punctureshopEnabled && (punctureshop == 0 || punctureShopType == 0)) {
            callback.onResponse(false, context.getString(R.string.choose_puncture))
        } else if (dailyutilityEnabled && (dailyutilityshop == 0 || utilityShopType == 0)) {
            callback.onResponse(false, context.getString(R.string.choose_daily_utility))
        } else if (barberEnabled && (barber == 0 || barberShopType == 0)) {
            callback.onResponse(false, context.getString(R.string.choose_barber_option))
        } else {
            callback.onResponse(true, "")
        }
    }
}