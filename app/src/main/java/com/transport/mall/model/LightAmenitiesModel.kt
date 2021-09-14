package com.transport.mall.model

import android.content.Context
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class LightAmenitiesModel : Serializable {
    var service_id = ""
    var module_id = ""
    var dhaba_id = ""
    var tower_light: Boolean = false
    var tower_image: String = ""
    var bulb_light: Boolean = false
    var bulb_image: String = ""
    var twentyfour_seven_electricity: Boolean = false

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (tower_light && tower_image.isEmpty()) {
            callback.onResponse(false, "Please choose Tower Light photo")
        } else if (bulb_light && bulb_image.isEmpty()) {
            callback.onResponse(false, "Please choose Bulb Light photo")
        } else {
            callback.onResponse(true, "")
        }
    }
}