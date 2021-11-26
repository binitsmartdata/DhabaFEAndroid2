package com.transport.mall.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class FoodAmenitiesModel : Serializable {
    @SerializedName(value = "_id")
    var _id: String = ""

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

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        /*if (foodLisence.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_food_license_option))
        } else*/ if (foodLisence.equals("true", true) && foodLisenceFile.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.select_license_photo))
        } /*else if (foodAt100.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_food_1_cost))
        } else if (roCleanWater.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.select_ro_water_option))
        } else if (food.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.select_food_type))
        } else if (images.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_atlease_one_photo))
        } */else {
            callback.onResponse(true, "")
        }
    }
}