package com.transport.mall.model

import android.content.Context
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class SleepingAmenitiesModel : Serializable {
    var service_id = ""
    var module_id = ""
    var dhaba_id = ""
    var sleeping = ""
    var noOfBeds = ""
    var fan = ""
    var cooler = ""
    var enclosed = ""
    var open = ""
    var hotWater = ""
    var images = ""

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (sleeping.isEmpty()) {
            callback.onResponse(false, "Please Choose Bed/Charpai option")
        } else if (sleeping.isNotEmpty() && noOfBeds.isEmpty()) {
            callback.onResponse(false, "Please Choose number of beds")
        } else if (images.isEmpty()) {
            callback.onResponse(false, "Please Choose a photo")
        } else if (fan.isEmpty()) {
            callback.onResponse(false, "Please Choose fan option")
        } else if (cooler.isEmpty()) {
            callback.onResponse(false, "Please Choose cooler option")
        } else if (enclosed.isEmpty()) {
            callback.onResponse(false, "Please Choose enclosed option")
        } else if (open.isEmpty()) {
            callback.onResponse(false, "Please Choose if it is open")
        } else if (hotWater.isEmpty()) {
            callback.onResponse(false, "Please Choose Hot Water Bag option")
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