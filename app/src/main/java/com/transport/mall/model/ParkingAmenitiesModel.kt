package com.transport.mall.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class ParkingAmenitiesModel : Serializable {
    @SerializedName(value = "_id")
    var _id = ""

    @SerializedName(value = "service_id")
    var service_id = "1"

    @SerializedName(value = "module_id")
    var module_id = "1"

    @SerializedName(value = "dhaba_id")
    var dhaba_id = ""

    @SerializedName(value = "concreteParking")
    var concreteParking = "0"

    @SerializedName(value = "flatHardParking")
    var flatHardParking = "0"

    @SerializedName(value = "kachaFlatParking")
    var kachaFlatParking = "0"

    @SerializedName(value = "parkingSpace")
    var parkingSpace = "0"

    @SerializedName(value = "images")
    var images = ArrayList<PhotosModel>()

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (images.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_atlease_one_photo))
        } else {
            callback.onResponse(true, "")
        }
    }

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

}