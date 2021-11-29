package com.transport.mall.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class WashroomAmenitiesModel : Serializable {
    @SerializedName(value = "_id")
    var _id = ""

    @SerializedName(value = "service_id")
    var service_id = "1"

    @SerializedName(value = "module_id")
    var module_id = "1"

    @SerializedName(value = "dhaba_id")
    var dhaba_id = ""

    @SerializedName(value = "washroomStatus")
    var washroomStatus = "false"

    @SerializedName(value = "water")
    var water = "false"

    @SerializedName(value = "cleaner")
    var cleaner = "false"

    @SerializedName(value = "images")
    var images: ArrayList<PhotosModel> = ArrayList()

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        /*if (washroomStatus.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_washroom_status))
        } else if (water.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_hot_and_cold_water))
        } else if (cleaner.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_truck_show_clean))
        } else */if (washroomStatus.isNotEmpty() && washroomStatus.equals("true", true) && images.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_photo))
        } else {
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