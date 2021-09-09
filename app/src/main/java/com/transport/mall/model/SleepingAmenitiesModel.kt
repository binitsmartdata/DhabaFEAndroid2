package com.transport.mall.model

import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class SleepingAmenitiesModel : Serializable {
    var service_id = ""
    var module_id = ""
    var dhaba_id = ""
    var sleeping = ""
    var fan = ""
    var enclosed = ""
    var open = ""
    var hotWater = ""

    fun hasEverything(callback: GenericCallBackTwoParams<Boolean, String>) {
        if (sleeping.isEmpty()) {
            callback.onResponse(false, "Please Choose Bed/Charpai option")
        } else if (fan.isEmpty()) {
            callback.onResponse(false, "Please Choose fan option")
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