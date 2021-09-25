package com.transport.mall.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaOwnerModel : Serializable, BaseObservable() {

    @SerializedName(value = "_id")
    var _id: String = ""
        @Bindable get() = field
        set(_id) {
            field = _id
            notifyPropertyChanged(BR._id)
        }

    @SerializedName(value = "ownerName", alternate = ["fname", "name"])
    var ownerName: String = ""
        @Bindable get() = field
        set(ownerName) {
            field = ownerName
            notifyPropertyChanged(BR.ownerName)
        }

    @SerializedName(value = "mobilePrefix")
    var mobilePrefix: String = ""
        @Bindable get() = field
        set(mobilePrefix) {
            field = mobilePrefix
            notifyPropertyChanged(BR.mobilePrefix)
        }

    @SerializedName(value = "mobile")
    var mobile: String = ""
        @Bindable get() = field
        set(mobile) {
            field = mobile
            notifyPropertyChanged(BR.mobile)
        }

    @SerializedName(value = "email")
    var email: String = ""
        @Bindable get() = field
        set(email) {
            field = email
            notifyPropertyChanged(BR.email)
        }

    @SerializedName(value = "address")
    var address: String = ""
        @Bindable get() = field
        set(address) {
            field = address
            notifyPropertyChanged(BR.address)
        }

/*
    @SerializedName(value = "location")
    var location: String = ""
        @Bindable get() = field
        set(location) {
            field = location
            notifyPropertyChanged(BR.location)
        }
*/

    @SerializedName(value = "panNumber")
    var panNumber: String = ""
        @Bindable get() = field
        set(panNumber) {
            field = panNumber
            notifyPropertyChanged(BR.panNumber)
        }

    @SerializedName(value = "adharCard", alternate = ["aadharNumber"])
    var adharCard: String = ""
        @Bindable get() = field
        set(adharCard) {
            field = adharCard
            notifyPropertyChanged(BR.adharCard)
        }

    @SerializedName(value = "ownerPic", alternate = ["profileImage"])
    var ownerPic: String = ""
        @Bindable get() = field
        set(ownerPic) {
            field = ownerPic
            notifyPropertyChanged(BR.ownerPic)
        }

    @SerializedName(value = "idproofFront")
    var idproofFront: String = ""
        @Bindable get() = field
        set(idproofFront) {
            field = idproofFront
            notifyPropertyChanged(BR.idproofFront)
        }

    @SerializedName(value = "idproofBack")
    var idproofBack: String = ""
        @Bindable get() = field
        set(idproofBack) {
            field = idproofBack
            notifyPropertyChanged(BR.idproofBack)
        }

    @SerializedName(value = "latitude")
    var latitude: String = ""
        @Bindable get() = field
        set(latitude) {
            field = latitude
            notifyPropertyChanged(BR.latitude)
        }

    @SerializedName(value = "longitude")
    var longitude: String = ""
        @Bindable get() = field
        set(longitude) {
            field = longitude
            notifyPropertyChanged(BR.longitude)
        }

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
        } /*else if (idproofFront.isEmpty()) {
            callback.onResponse(false, "Please Select ID proof front image")
        } else if (idproofBack.isEmpty()) {
            callback.onResponse(false, "Please Select ID proof back image")
        } */ else {
            callback.onResponse(true, "")
        }
    }
}