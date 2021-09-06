package com.transport.mall.model

import com.google.gson.annotations.SerializedName
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaOwnerModel : Serializable {
    var _id: String = ""

    @SerializedName(value = "ownerName", alternate = ["fname"])
    var ownerName: String = ""

    @SerializedName(value = "mobile")
    var mobile: String = ""

    @SerializedName(value = "email")
    var email: String = ""

    @SerializedName(value = "address")
    var address: String = ""

    @SerializedName(value = "panNumber")
    var panNumber: String = ""

    @SerializedName(value = "adharCard")
    var adharCard: String = ""

    @SerializedName(value = "ownerPic", alternate = ["profileImage"])
    var ownerPic: String = ""

    @SerializedName(value = "idproofFront")
    var idproofFront: String = ""

    @SerializedName(value = "idproofBack")
    var idproofBack: String = ""


    fun hasEverything(callback: GenericCallBackTwoParams<Boolean, String>) {
        if (ownerName.isEmpty()) {
            callback.onResponse(false, "Please Enter Owner Name")
        } else if (mobile.isEmpty()) {
            callback.onResponse(false, "Please enter Phone Number")
        } else if (email.isEmpty()) {
            callback.onResponse(false, "Please Enter Email Id")
        } else if (!GlobalUtils.isValidEmail(email)) {
            callback.onResponse(false, "Please Enter Valid Email Id")
        } else if (address.isEmpty()) {
            callback.onResponse(false, "Please Enter Address")
        } else if (panNumber.isEmpty()) {
            callback.onResponse(false, "Please Enter Pan Number")
        } else if (adharCard.isEmpty()) {
            callback.onResponse(false, "Please Aadhar Card Number")
        } else if (ownerPic.isEmpty()) {
            callback.onResponse(false, "Please Select Owner Picture")
        } else if (idproofFront.isEmpty()) {
            callback.onResponse(false, "Please Select ID proof front image")
        } else if (idproofBack.isEmpty()) {
            callback.onResponse(false, "Please Select ID proof back image")
        } else {
            callback.onResponse(true, "")
        }
    }
}