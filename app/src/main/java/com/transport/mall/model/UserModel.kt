package com.transport.mall.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserModel : Serializable{
    @SerializedName("fname")
    var fname: String = ""

    @SerializedName("lname")
    var lname: String = ""

    @SerializedName("email")
    var email: String = ""

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

    @SerializedName("mobile")
    var mobile: String = ""

    @SerializedName("address")
    var address: String = ""

    @SerializedName("profileImage")
    var profileImage: String = ""

    @SerializedName("idproofFront")
    var idproofFront: String = ""

    @SerializedName("idproofBack")
    var idproofBack: String = ""

    @SerializedName("createdAt")
    var createdAt: String = ""

    @SerializedName("updatedAt")
    var updatedAt: String = ""
}