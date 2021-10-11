package com.transport.mall.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserModel : Serializable, BaseObservable() {
    @SerializedName(value = "fname", alternate = ["name", "ownerName"])
    var name: String = ""
        @Bindable get() = field
        set(name) {
            field = name
            notifyPropertyChanged(BR.name)
        }

    @SerializedName("email")
    var email: String = ""
        @Bindable get() = field
        set(email) {
            field = email
            notifyPropertyChanged(BR.email)
        }

    @SerializedName("lastLogin")
    var lastLogin: String = ""

    @SerializedName("accessToken")
    var accessToken: String = ""

    @SerializedName("service")
    var service: String = ""

    @SerializedName("isEmailVerified")
    var isEmailVerified: String = ""

    @SerializedName("isMobileVerified")
    var isMobileVerified: String = ""

    @SerializedName("role")
    var role: String = ""

    @SerializedName("panNumber")
    var panNumber: String = ""

    @SerializedName("panFile")
    var panFile: String = ""

    @SerializedName("aadharNumber")
    var aadharNumber: String = ""

    @SerializedName("language")
    var language: String = ""

    @SerializedName("deviceToken")
    var deviceToken: String = ""

    @SerializedName("deviceType")
    var deviceType: String = ""

    @SerializedName("isDeleted")
    var isDeleted: String = ""

    @SerializedName("isActive")
    var isActive: String = ""

    @SerializedName("isLogin")
    var isLogin: String = ""

    @SerializedName("_id")
    var _id: String = ""

    @SerializedName("mobilePrefix")
    var mobilePrefix: String = ""
        @Bindable get() = field
        set(mobilePrefix) {
            field = mobilePrefix
            notifyPropertyChanged(BR.mobilePrefix)
        }

    @SerializedName("mobile")
    var mobile: String = ""
        @Bindable get() = field
        set(mobile) {
            field = mobile
            notifyPropertyChanged(BR.mobile)
        }

    @SerializedName("address")
    var address: String = ""

    @SerializedName("profileImage")
    var profileImage: String = ""
        @Bindable get() = field
        set(profileImage) {
            field = profileImage
            notifyPropertyChanged(BR.profileImage)
        }

    @SerializedName("idproofFront")
    var idproofFront: String = ""

    @SerializedName("idproofBack")
    var idproofBack: String = ""

    @SerializedName("createdAt")
    var createdAt: String = ""

    @SerializedName("updatedAt")
    var updatedAt: String = ""

    companion object {
        val ROLE_OWNER = "owner"
        val ROLE_EXECUTIVE = "executive"
    }

    fun isOwner(): Boolean {
//        return role.equals(ROLE_OWNER, true)
        return true
    }

    fun isExecutive(): Boolean {
        return role.equals(ROLE_EXECUTIVE, true)
    }
}