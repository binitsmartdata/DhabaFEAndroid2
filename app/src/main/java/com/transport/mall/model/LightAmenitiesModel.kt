package com.transport.mall.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class LightAmenitiesModel : Serializable {
    @SerializedName(value = "_id")
    var _id = ""

    @SerializedName(value = "service_id")
    var service_id = "1"

    @SerializedName(value = "module_id")
    var module_id = "1"

    @SerializedName(value = "dhaba_id")
    var dhaba_id = ""

    @SerializedName(value = "towerLight")
    var towerLight: Boolean = false

    @SerializedName(value = "towerLightImage")
    var towerLightImage: String = ""

    @SerializedName(value = "bulbLight")
    var bulbLight: Boolean = false

    @SerializedName(value = "bulbLightImage")
    var bulbLightImage: String = ""

    @SerializedName(value = "electricityBackup")
    var electricityBackup: Boolean = false

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (towerLight && towerLightImage.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_tower_light_photo))
        } else if (bulbLight && bulbLightImage.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_bulb_light_photo))
        } else {
            callback.onResponse(true, "")
        }
    }
}