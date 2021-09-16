package com.transport.mall.model

import android.content.Context
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class LightAmenitiesModel : Serializable {
    var service_id = "1"
    var module_id = "1"
    var dhaba_id = ""
    var towerLight: Boolean = false
    var towerLightImage: String = ""
    var bulbLight: Boolean = false
    var bulbLightImage: String = ""
    var electricityBackup: Boolean = false

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (towerLight && towerLightImage.isEmpty()) {
            callback.onResponse(false, "Please choose Tower Light photo")
        } else if (bulbLight && bulbLightImage.isEmpty()) {
            callback.onResponse(false, "Please choose Bulb Light photo")
        } else {
            callback.onResponse(true, "")
        }
    }
}