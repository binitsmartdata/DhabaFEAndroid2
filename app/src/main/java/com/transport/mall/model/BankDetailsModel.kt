package com.transport.mall.model

import com.google.gson.annotations.SerializedName
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class BankDetailsModel : Serializable {
    var _id: String = ""

    @SerializedName(value = "user_id")
    var user_id: String = ""

    @SerializedName(value = "bankName")
    var bankName: String = ""

    @SerializedName(value = "gstNumber")
    var gstNumber: String = ""

    @SerializedName(value = "ifscCode")
    var ifscCode: String = ""

    @SerializedName(value = "accountName")
    var accountName: String = ""

    @SerializedName(value = "panNumber")
    var panNumber: String = ""

    @SerializedName(value = "panPhoto")
    var panPhoto: String = ""

    fun hasEverything(callback: GenericCallBackTwoParams<Boolean, String>) {
        if (bankName.isEmpty()) {
            callback.onResponse(false, "Please Enter Bank Name")
        } else if (gstNumber.isEmpty()) {
            callback.onResponse(false, "Please Enter GST Number")
        } else if (ifscCode.isEmpty()) {
            callback.onResponse(false, "Please Enter IFSC Code")
        } else if (accountName.isEmpty()) {
            callback.onResponse(false, "Please Enter Account Holder Name")
        } else if (panNumber.isEmpty()) {
            callback.onResponse(false, "Please Enter Pan Number")
        } else if (panPhoto.isEmpty()) {
            callback.onResponse(false, "Please Choose Pan Card Photo")
        } else {
            callback.onResponse(true, "")
        }
    }
}