package com.transport.mall.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

class SecurityAmenitiesModel : Serializable {
    @SerializedName(value = "_id")
    var _id = ""

    @SerializedName(value = "service_id")
    var service_id = "1"

    @SerializedName(value = "module_id")
    var module_id = "1"

    @SerializedName(value = "dhaba_id")
    var dhaba_id = ""

    @SerializedName(value = "dayGuard")
    var dayGuard: Int = 0

    @SerializedName(value = "nightGuard")
    var nightGuard: Int = 0

    @SerializedName(value = "policVerification")
    var policVerification: Boolean = false

    @SerializedName(value = "verificationImg")
    var verificationImg: String = ""

    @SerializedName(value = "indoorCamera")
    var indoorCamera: Int = 0

    @SerializedName(value = "indoorCameraImage")
    var indoorCameraImage: ArrayList<PhotosModel> = ArrayList()

    @SerializedName(value = "outdoorCamera")
    var outdoorCamera: Int = 0

    @SerializedName(value = "outdoorCameraImage")
    var outdoorCameraImage: ArrayList<PhotosModel> = ArrayList()

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (policVerification && verificationImg.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_verification_photo))
        } else if (indoorCamera > 0 && indoorCameraImage.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_indoor_camera_photo))
        } else if (outdoorCamera > 0 && outdoorCameraImage.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.choose_outdoor_camera_photo))
        } else {
            callback.onResponse(true, "")
        }
    }
}