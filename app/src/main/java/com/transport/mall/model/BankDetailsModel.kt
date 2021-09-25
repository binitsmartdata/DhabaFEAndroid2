package com.transport.mall.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class BankDetailsModel : Serializable, BaseObservable() {
    @SerializedName(value = "_id")
    var _id: String = ""

    @SerializedName(value = "user_id")
    var user_id: String = ""
        @Bindable get() = field
        set(user_id) {
            field = user_id
            notifyPropertyChanged(BR.user_id)
        }

    @SerializedName(value = "bankName")
    var bankName: String = ""
        @Bindable get() = field
        set(bankName) {
            field = bankName
            notifyPropertyChanged(BR.bankName)
        }

    @SerializedName(value = "gstNumber")
    var gstNumber: String = ""
        @Bindable get() = field
        set(gstNumber) {
            field = gstNumber
            notifyPropertyChanged(BR.gstNumber)
        }

    @SerializedName(value = "ifscCode")
    var ifscCode: String = ""
        @Bindable get() = field
        set(ifscCode) {
            field = ifscCode
            notifyPropertyChanged(BR.ifscCode)
        }

    @SerializedName(value = "accountName")
    var accountName: String = ""
        @Bindable get() = field
        set(accountName) {
            field = accountName
            notifyPropertyChanged(BR.accountName)
        }

    @SerializedName(value = "panNumber")
    var panNumber: String = ""
        @Bindable get() = field
        set(panNumber) {
            field = panNumber
            notifyPropertyChanged(BR.panNumber)
        }

    @SerializedName(value = "panPhoto")
    var panPhoto: String = ""
        @Bindable get() = field
        set(panPhoto) {
            field = panPhoto
            notifyPropertyChanged(BR.panPhoto)
        }

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