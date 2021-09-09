package com.transport.mall.model

import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class ParkingAmenitiesModel : Serializable {
    var service_id = ""
    var module_id = ""
    var dhaba_id = ""
    var concreteParking = "0"
    var flatHardParking = "0"
    var kachaFlatParking = "0"
    var parkingSpace = "0"
    var images = ArrayList<PhotosModel>()

    fun hasEverything(callback: GenericCallBackTwoParams<Boolean, String>) {
        if (images.isEmpty()) {
            callback.onResponse(false, "Please Choose at lease 1 Photo")
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