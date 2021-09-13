package com.transport.mall.model

import android.app.Activity
import android.content.Context
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class WashroomAmenitiesModel : Serializable {
    var service_id = ""
    var module_id = ""
    var dhaba_id = ""
    var washroomStatus = ""
    var water = ""
    var cleaner = ""
    var images = ""

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (washroomStatus.isEmpty()) {
            callback.onResponse(false, "Please choose washroom status.")
        } else if (water.isEmpty()) {
            callback.onResponse(false, "Please choose hot and cold water option.")
        } else if (cleaner.isEmpty()) {
            callback.onResponse(false, "Please choose truck show clean option.")
        } else if (images.isEmpty()) {
            callback.onResponse(false, "Please choose a photo")
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