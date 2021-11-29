package com.transport.mall.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class SleepingAmenitiesModel : Serializable {
    @SerializedName(value = "_id")
    var _id = ""

    @SerializedName(value = "service_id")
    var service_id = "1"

    @SerializedName(value = "module_id")
    var module_id = "1"

    @SerializedName(value = "dhaba_id")
    var dhaba_id = ""

    @SerializedName(value = "sleeping")
    var sleeping = "false"

    @SerializedName(value = "noOfBeds")
    var noOfBeds = "0"

    @SerializedName(value = "fan")
    var fan = "false"

    @SerializedName(value = "cooler")
    var cooler = "false"

    @SerializedName(value = "enclosed")
    var enclosed = ""

    @SerializedName(value = "open")
    var open = "false"

    @SerializedName(value = "hotWater")
    var hotWater = "false"

    @SerializedName(value = "images")
    var images = ""

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        /*if (sleeping.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_bed_charpai))
        } else */if (sleeping.isNotEmpty() && sleeping.equals("true") && noOfBeds.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_no_of_beds))
        } else if (sleeping.equals("true") && images.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_photo))
        } /*else if (fan.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_fan))
        } else if (cooler.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_cooler))
        } else if (enclosed.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_sleeping_option))
        } else if (open.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_if_open))
        } else if (hotWater.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_hot_water))
        }*/ else {
            callback.onResponse(true, "")
        }
    }

/*
    fun getNoOfAmenities(): Int {
        var count = 0
        if (concreteParking.isNotEmpty()) {
            count += 1
        }
        if (flatHardParking.isNotEmpty()) {
            count += 1
        }
        if (kachaFlatParking.isNotEmpty()) {
            count += 1
        }
        if (parkingSpace.isNotEmpty()) {
            count += 1
        }
        if (images.isNotEmpty()) {
            count += 1
        }
        return count
    }
*/

}