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

    @SerializedName(value = "mechanicShop")
    var mechanicShop: Int = 0
    var mechanicShopEnabled: Boolean = false

    @SerializedName(value = "mechanicShopDay")
    var mechanicShopDay: Int = 0
    var mechanicShopDayEnabled: Boolean = false

    @SerializedName(value = "punctureshop")
    var punctureshop: Int = 0
    var punctureshopEnabled: Boolean = false

    @SerializedName(value = "punctureshopDay")
    var punctureshopDay: Int = 0
    var punctureshopDayEnabled: Boolean = false

    @SerializedName(value = "dailyutilityshop")
    var dailyutilityshop: Int = 0
    var dailyutilityEnabled: Boolean = false

    @SerializedName(value = "dailyutilityshopDay")
    var dailyutilityshopDay: Int = 0
    var dailyutilityDayEnabled: Boolean = false

    @SerializedName(value = "barber")
    var barber: Int = 0

    @SerializedName(value = "barberImages")
    var barberImages: ArrayList<PhotosModel> = ArrayList()

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (mechanicShopEnabled && mechanicShop == 0) {
            callback.onResponse(false, context.getString(R.string.choose_mechanic))
        } else if (mechanicShopDayEnabled && mechanicShopDay == 0) {
            callback.onResponse(false, context.getString(R.string.choose_mechanic_day))
        } else if (punctureshopEnabled && punctureshop == 0) {
            callback.onResponse(false, context.getString(R.string.choose_puncture))
        } else if (punctureshopDayEnabled && punctureshopDay == 0) {
            callback.onResponse(false, context.getString(R.string.choose_puncture_day))
        } else if (dailyutilityEnabled && dailyutilityshop == 0) {
            callback.onResponse(false, context.getString(R.string.choose_daily_utility))
        } else if (dailyutilityDayEnabled && dailyutilityshopDay == 0) {
            callback.onResponse(false, context.getString(R.string.choose_utility_day))
        } else {
            callback.onResponse(true, "")
        }
    }
}