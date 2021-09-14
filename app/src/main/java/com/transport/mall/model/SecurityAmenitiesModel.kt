package com.transport.mall.model

import android.content.Context
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class SecurityAmenitiesModel : Serializable {
    var service_id = ""
    var module_id = ""
    var dhaba_id = ""
    var dayGuard: Int = 0
    var nightGuard: Int = 0
    var policVerification: Boolean = false
    var verificationImg: String = ""
    var indoorCamera: Int = 0
    var indoorCameraImage: ArrayList<PhotosModel> = ArrayList()
    var outdoorCamera: Int = 0
    var outdoorCameraImage: ArrayList<PhotosModel> = ArrayList()

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (policVerification && verificationImg.isEmpty()) {
            callback.onResponse(false, "Please choose police verification photo.")
        } else if (indoorCamera > 0 && indoorCameraImage.isEmpty()) {
            callback.onResponse(false, "Please choose photos of indoor camera(s).")
        } else if (outdoorCamera > 0 && outdoorCameraImage.isEmpty()) {
            callback.onResponse(false, "Please choose photos of outdoor camera(s).")
        } else {
            callback.onResponse(true, "")
        }
    }
}