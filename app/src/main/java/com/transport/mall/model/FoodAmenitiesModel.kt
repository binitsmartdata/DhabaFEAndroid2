package com.transport.mall.model

import com.google.gson.annotations.SerializedName
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class FoodAmenitiesModel : Serializable {
    @SerializedName(value = "service_id")
    var service_id: String = "1"

    @SerializedName(value = "module_id")
    var module_id: String = "1"

    @SerializedName(value = "dhaba_id")
    var dhaba_id: String = ""

    @SerializedName(value = "name")
    var name: String = ""

    @SerializedName(value = "foodLisence")
    var foodLisence: String = ""

    @SerializedName(value = "foodAt100")
    var foodAt100: String = ""

    @SerializedName(value = "roCleanWater")
    var roCleanWater: String = ""

    @SerializedName(value = "normalWater")
    var normalWater: String = ""

    @SerializedName(value = "food")
    var food: String = ""

    @SerializedName(value = "images")
    var images: ArrayList<PhotosModel> = ArrayList()

    @SerializedName(value = "foodLisenceFile")
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