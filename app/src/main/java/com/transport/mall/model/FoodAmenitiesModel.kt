package com.transport.mall.model

import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class FoodAmenitiesModel : Serializable {
    var service_id: String = ""
    var module_id: String = ""
    var dhaba_id: String = ""
    var name: String = ""
    var foodLisence: String = ""
    var foodAt100: String = ""
    var roCleanWater: String = ""
    var normalWater: String = ""
    var food: String = ""
    var images: ArrayList<PhotosModel> = ArrayList()
    var foodLisenceFile: String = ""

    fun hasEverything(callback: GenericCallBackTwoParams<Boolean, String>) {
        if (foodLisence.isEmpty()) {
            callback.onResponse(false, "Please Choose Food License Option")
        } else if (foodLisence.equals("true", true) && foodLisenceFile.isEmpty()) {
            callback.onResponse(false, "Please Select Food License Photo")
        } else if (foodAt100.isEmpty()) {
            callback.onResponse(false, "Please Choose Food at 100 option")
        } else if (roCleanWater.isEmpty()) {
            callback.onResponse(false, "Please Choose RO Water option")
        } else if (food.isEmpty()) {
            callback.onResponse(false, "Please Choose Food Type option")
        } else if (images.isEmpty()) {
            callback.onResponse(false, "Please Choose at lease 1 Food Photo")
        } else {
            callback.onResponse(true, "")
        }
    }
}